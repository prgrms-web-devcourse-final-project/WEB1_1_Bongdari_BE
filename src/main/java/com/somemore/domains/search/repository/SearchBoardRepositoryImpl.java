package com.somemore.domains.search.repository;

import co.elastic.clients.elasticsearch._types.GeoLocation;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.somemore.center.usecase.NEWCenterQueryUseCase;
import com.somemore.domains.community.domain.CommunityBoard;
import com.somemore.domains.location.domain.Location;
import com.somemore.domains.location.usecase.query.LocationQueryUseCase;
import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.domains.search.annotation.ConditionalOnElasticSearchEnabled;
import com.somemore.domains.search.domain.CommunityBoardDocument;
import com.somemore.domains.search.domain.RecruitBoardDocument;
import com.somemore.volunteer.usecase.NEWVolunteerQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
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
import java.util.function.Consumer;

@RequiredArgsConstructor
@Repository
@ConditionalOnElasticSearchEnabled
public class SearchBoardRepositoryImpl implements SearchBoardRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    private final RecruitBoardDocumentRepository recruitBoardDocumentRepository;
    private final CommunityBoardDocumentRepository communityBoardDocumentRepository;

    private final NEWVolunteerQueryUseCase volunteerQueryUseCase;
    private final NEWCenterQueryUseCase centerQueryUseCase;
    private final LocationQueryUseCase locationQueryUseCase;

    @Override
    public Page<RecruitBoardDocument> findByRecruitBoardsContaining(RecruitBoardSearchCondition condition) {
        NativeQuery searchQuery = getRecruitBoardWithSearchCondition(null, condition);

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

        List<RecruitBoardDocument> boardDocuments =
                elasticsearchOperations.search(searchQuery, RecruitBoardDocument.class)
                        .stream()
                        .map(SearchHit::getContent)
                        .toList();

        return PageableExecutionUtils.getPage(boardDocuments, condition.pageable(), boardDocuments::size);
    }

    @Override
    public Page<RecruitBoardDocument> findAllByCenterIdWithKeyword(UUID centerId, RecruitBoardSearchCondition condition) {
        NativeQuery searchQuery = getRecruitBoardWithSearchCondition(centerId, condition);

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

    private List<CommunityBoardDocument> getCommunityBoardDocuments(String keyword) {

        if (keyword == null || keyword.isEmpty()) {
            return communityBoardDocumentRepository.findAll();
        }
        return communityBoardDocumentRepository.findDocumentsByTitleOrContentOrNicknameContaining(keyword);
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

    private NativeQuery getRecruitBoardWithSearchCondition(UUID centerId, RecruitBoardSearchCondition condition) {
        return buildNativeQuery(builder -> {
            if (centerId != null) {
                builder.addMustQuery("centerId", centerId.toString());
            }
            if (condition.category() != null) {
                builder.addMustQuery("volunteerCategory", condition.category().toString());
            }
            if (condition.region() != null && !condition.region().isEmpty()) {
                builder.addMustQuery("region", condition.region());
            }
            if (condition.admitted() != null) {
                builder.addMustQuery("admitted", String.valueOf(condition.admitted()));
            }
            if (condition.status() != null) {
                builder.addMustQuery("recruitStatus", condition.status().toString());
            }
            if (condition.keyword() != null && !condition.keyword().isEmpty()) {
                builder.addShouldQuery(condition.keyword());
            }
        });
    }

    private NativeQuery getRecruitBoardWithNearByCondition(RecruitBoardNearByCondition condition) {
        return buildNativeQuery(builder -> {
            if (condition.latitude() != null && condition.longitude() != null && condition.radius() != null) {
                builder.addGeoDistanceQuery(condition.latitude(), condition.longitude(), condition.radius());
            }
            if (condition.status() != null) {
                builder.addMustQuery("recruitStatus", condition.status().toString());
            }
            if (condition.keyword() != null && !condition.keyword().isEmpty()) {
                builder.addShouldQuery(condition.keyword());
            }
        });
    }

    private NativeQuery buildNativeQuery(Consumer<QueryBuilder> builderConsumer) {
        QueryBuilder builder = new QueryBuilder();
        builderConsumer.accept(builder);
        return NativeQuery.builder()
                .withQuery(builder.build())
                .build();
    }

    private static class QueryBuilder {
        private final List<Query> mustQueries = new ArrayList<>();
        private final List<Query> shouldQueries = new ArrayList<>();

        void addMustQuery(String field, String value) {
            mustQueries.add(QueryBuilders.term()
                    .field(field)
                    .value(value)
                    .build()
                    ._toQuery());
        }

        void addShouldQuery(String keyword) {
            shouldQueries.add(
                    QueryBuilders.multiMatch()
                            .fields("title^3", "content^2", "centerName")
                            .query(keyword)
                            .fuzziness("AUTO")
                            .build()
                            ._toQuery()
            );
        }

        void addGeoDistanceQuery(double latitude, double longitude, double radius) {
            String distance = radius + "km";
            mustQueries.add(QueryBuilders.geoDistance()
                    .field("location")
                    .distance(distance)
                    .location(GeoLocation.of(builder -> builder
                            .latlon(latlon -> latlon
                                    .lat(latitude)
                                    .lon(longitude)
                            )
                    ))
                    .build()
                    ._toQuery());
        }

        Query build() {
            if (mustQueries.isEmpty() && shouldQueries.isEmpty()) {
                return QueryBuilders.matchAll().build()._toQuery();
            } else {
                return new BoolQuery.Builder()
                        .must(mustQueries)
                        .should(shouldQueries)
                        .build()
                        ._toQuery();
            }
        }
    }
}
