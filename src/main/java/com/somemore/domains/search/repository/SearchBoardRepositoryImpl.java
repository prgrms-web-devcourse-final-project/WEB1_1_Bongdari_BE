package com.somemore.domains.search.repository;

import co.elastic.clients.elasticsearch._types.GeoLocation;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.somemore.domains.center.usecase.query.CenterQueryUseCase;
import com.somemore.domains.community.domain.CommunityBoard;
import com.somemore.domains.location.domain.Location;
import com.somemore.domains.location.usecase.query.LocationQueryUseCase;
import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.domains.search.domain.CommunityBoardDocument;
import com.somemore.domains.search.domain.RecruitBoardDocument;
import com.somemore.domains.volunteer.usecase.VolunteerQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
@ConditionalOnProperty(name = "elastic.search.enabled", havingValue = "true", matchIfMissing = true)
public class SearchBoardRepositoryImpl implements SearchBoardRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    private final RecruitBoardDocumentRepository recruitBoardDocumentRepository;
    private final CommunityBoardDocumentRepository communityBoardDocumentRepository;

    private final VolunteerQueryUseCase volunteerQueryUseCase;
    private final CenterQueryUseCase centerQueryUseCase;
    private final LocationQueryUseCase locationQueryUseCase;

    @Override
    public Page<RecruitBoardDocument> findByRecruitBoardsContaining(RecruitBoardSearchCondition condition) {
        NativeQuery searchQuery = getRecruitBoardWithSearchCondition(condition);

        List<RecruitBoardDocument> boardDocuments =
                elasticsearchOperations.search(searchQuery, RecruitBoardDocument.class)
                        .stream()
                        .map(SearchHit::getContent)
                        .toList();

        return PageableExecutionUtils.getPage(boardDocuments, condition.pageable(), boardDocuments::size);
    }

    @Override
    public Page<RecruitBoardDocument> findAllNearbyWithKeyword(RecruitBoardNearByCondition condition) {
        NativeQuery searchQuery = getRecruitBoardWithNearByCondition(condition);

        System.out.println("Native Query : " + searchQuery.getQuery());

        List<RecruitBoardDocument> boardDocuments =
                elasticsearchOperations.search(searchQuery, RecruitBoardDocument.class)
                        .stream()
                        .map(SearchHit::getContent)
                        .toList();

        return PageableExecutionUtils.getPage(boardDocuments, condition.pageable(), boardDocuments::size);
    }

    @Override
    public void saveRecruitBoardDocuments(List<RecruitBoard> recruitBoards) {
        List<RecruitBoardDocument> recruitBoardDocuments = convertRecruitBoardToDocuments(recruitBoards);
        recruitBoardDocumentRepository.saveAll(recruitBoardDocuments);
    }

    @Override
    public void deleteRecruitBoardDocument(Long id) {
        recruitBoardDocumentRepository.deleteById(id);
    }

    @Override
    public Page<CommunityBoardDocument> findByCommunityBoardsContaining(String keyword, Pageable pageable) {
        List<CommunityBoardDocument> boardDocuments = getCommunityBoardDocuments(keyword);
        return PageableExecutionUtils.getPage(boardDocuments, pageable, boardDocuments::size);
    }

    @Override
    public void saveCommunityBoardDocuments(List<CommunityBoard> communityBoards) {
        List<CommunityBoardDocument> communityBoardDocuments = convertCommunityBoardToDocuments(communityBoards);
        communityBoardDocumentRepository.saveAll(communityBoardDocuments);
    }

    @Override
    public void deleteAllCommunityBoardDocument() {
        communityBoardDocumentRepository.deleteAll();
    }

    private List<RecruitBoardDocument> convertRecruitBoardToDocuments(List<RecruitBoard> recruitBoards) {
        List<RecruitBoardDocument> communityBoardDocuments = new ArrayList<>();

        for (RecruitBoard recruitBoard : recruitBoards) {
            UUID centerId = recruitBoard.getCenterId();
            String centerName = centerQueryUseCase.getNameById(centerId);

            Long locationId =  recruitBoard.getLocationId();
            Location location = locationQueryUseCase.getById(locationId);

            RecruitBoardDocument document = RecruitBoardDocument.builder()
                    .id(recruitBoard.getId())
                    .title(recruitBoard.getTitle())
                    .content(recruitBoard.getContent())
                    .centerName(centerName)
                    .locationId(locationId)
                    .createdAt(recruitBoard.getCreatedAt())
                    .updatedAt(recruitBoard.getUpdatedAt())
                    .region(recruitBoard.getRecruitmentInfo().getRegion())
                    .recruitStatus(recruitBoard.getRecruitStatus())
                    .recruitmentCount(recruitBoard.getRecruitmentInfo().getRecruitmentCount())
                    .volunteerStartDateTime(recruitBoard.getRecruitmentInfo().getVolunteerStartDateTime())
                    .volunteerEndDateTime(recruitBoard.getRecruitmentInfo().getVolunteerEndDateTime())
                    .volunteerCategory(recruitBoard.getRecruitmentInfo().getVolunteerCategory())
                    .volunteerHours(recruitBoard.getRecruitmentInfo().getVolunteerHours())
                    .admitted(recruitBoard.getRecruitmentInfo().getAdmitted())
                    .centerId(centerId)
                    .address(location.getAddress())
                    .location(new double[]{location.getLongitude().doubleValue(),
                            location.getLatitude().doubleValue()})
                    .build();
            communityBoardDocuments.add(document);
        }
        return communityBoardDocuments;
    }

    private List<CommunityBoardDocument> convertCommunityBoardToDocuments(List<CommunityBoard> communityBoards) {
        List<CommunityBoardDocument> communityBoardDocuments = new ArrayList<>();

        for (CommunityBoard communityboard : communityBoards) {
            String nickname = volunteerQueryUseCase.getNicknameById(communityboard.getWriterId());

            CommunityBoardDocument document = CommunityBoardDocument.builder()
                    .id(communityboard.getId())
                    .title(communityboard.getTitle())
                    .content(communityboard.getContent())
                    .writerNickname(nickname)
                    .createdAt(communityboard.getCreatedAt())
                    .updatedAt(communityboard.getUpdatedAt())
                    .build();
            communityBoardDocuments.add(document);
        }
        return communityBoardDocuments;
    }

    private NativeQuery getRecruitBoardWithSearchCondition(RecruitBoardSearchCondition condition) {
        Query query;
        List<Query> mustQueries = new ArrayList<>();
        List<Query> shouldQueries = new ArrayList<>();

        if (condition.category() != null) {
            mustQueries.add(QueryBuilders.term()
                    .field("volunteerCategory")
                    .value(condition.category().toString())
                    .build()
                    ._toQuery());
        }
        if (condition.region() != null && !condition.region().isEmpty()) {
            mustQueries.add(QueryBuilders.term()
                    .field("region")
                    .value(condition.region())
                    .build()
                    ._toQuery());
        }
        if (condition.admitted() != null) {
            mustQueries.add(QueryBuilders.term()
                    .field("admitted")
                    .value(condition.admitted())
                    .build()
                    ._toQuery());
        }
        if (condition.status() != null) {
            mustQueries.add(QueryBuilders.term()
                    .field("recruitStatus")
                    .value(condition.status().toString())
                    .build()
                    ._toQuery());
        }
        if (condition.keyword() != null && !condition.keyword().isEmpty()) {
            shouldQueries.add(
                    QueryBuilders.multiMatch()
                            .fields("title^3", "content^2", "centerName")
                            .query(condition.keyword())
                            .fuzziness("AUTO")
                            .build()
                            ._toQuery()
            );
        }

        if (mustQueries.isEmpty() && shouldQueries.isEmpty()) {
            query = QueryBuilders.matchAll().build()._toQuery();
        } else {
            query = new BoolQuery.Builder()
                    .must(mustQueries)
                    .should(shouldQueries)
                    .build()
                    ._toQuery();
        }

        return NativeQuery.builder()
                .withQuery(query)
                .build();
    }

    private NativeQuery getRecruitBoardWithNearByCondition(RecruitBoardNearByCondition condition) {
        Query query;
        List<Query> mustQueries = new ArrayList<>();
        List<Query> shouldQueries = new ArrayList<>();

        if (condition.latitude() != null && condition.longitude() != null && condition.radius() != null) {
            String distance = condition.radius() + "km";

            mustQueries.add(QueryBuilders.geoDistance()
                    .field("location")
                    .distance(distance)
                    .location(GeoLocation.of(builder -> builder
                            .latlon(latlon -> latlon
                                    .lat(condition.latitude())
                                    .lon(condition.longitude())
                            )
                    ))
                    .build()
                    ._toQuery());
        }
        if (condition.status() != null) {
            mustQueries.add(QueryBuilders.term()
                    .field("recruitStatus")
                    .value(condition.status().toString())
                    .build()
                    ._toQuery());
        }
        if (condition.keyword() != null && !condition.keyword().isEmpty()) {
            shouldQueries.add(
                    QueryBuilders.multiMatch()
                            .fields("title^3", "content^2", "centerName")
                            .query(condition.keyword())
                            .fuzziness("AUTO")
                            .build()
                            ._toQuery()
            );
        }

        if (mustQueries.isEmpty() && shouldQueries.isEmpty()) {
            query = QueryBuilders.matchAll().build()._toQuery();
        } else {
            query = new BoolQuery.Builder()
                    .must(mustQueries)
                    .should(shouldQueries)
                    .build()
                    ._toQuery();
        }

        return NativeQuery.builder()
                .withQuery(query)
                .build();
    }

    private List<CommunityBoardDocument> getCommunityBoardDocuments(String keyword) {

        if (keyword == null || keyword.isEmpty()) {
            return communityBoardDocumentRepository.findAll();
        }
        return communityBoardDocumentRepository.findDocumentsByTitleOrContentOrNicknameContaining(keyword);
    }
}
