package com.somemore.domains.search.repository;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.domains.center.domain.QCenter;
import com.somemore.domains.community.domain.CommunityBoard;
import com.somemore.domains.community.domain.QCommunityBoard;
import com.somemore.domains.community.repository.mapper.CommunityBoardView;
import com.somemore.domains.location.domain.QLocation;
import com.somemore.domains.location.utils.GeoUtils;
import com.somemore.domains.recruitboard.domain.QRecruitBoard;
import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.domain.RecruitStatus;
import com.somemore.domains.recruitboard.domain.VolunteerCategory;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.domains.recruitboard.repository.mapper.RecruitBoardDetail;
import com.somemore.domains.recruitboard.repository.mapper.RecruitBoardWithCenter;
import com.somemore.domains.search.domain.CommunityBoardDocument;
import com.somemore.domains.search.domain.RecruitBoardDocument;
import com.somemore.domains.volunteer.domain.QVolunteer;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
@ConditionalOnProperty(name = "elastic.search.enabled", havingValue = "true", matchIfMissing = true)
public class SearchBoardRepositoryImpl implements SearchBoardRepository {

    private final RecruitBoardDocumentRepository recruitBoardDocumentRepository;
    private final CommunityBoardDocumentRepository communityBoardDocumentRepository;
    private final JPAQueryFactory queryFactory;

    private static final QRecruitBoard recruitBoard = QRecruitBoard.recruitBoard;
    private static final QLocation location = QLocation.location;
    private static final QCenter center = QCenter.center;

    private static final QCommunityBoard communityBoard = QCommunityBoard.communityBoard;
    private static final QVolunteer volunteer = QVolunteer.volunteer;

    @Override
    public Page<RecruitBoardWithCenter> findByRecruitBoardsContaining(RecruitBoardSearchCondition condition) {

        List<RecruitBoardDocument> boardDocuments = getRecruitBoardDocuments(condition.keyword());

        List<Long> boardIds = boardDocuments.stream()
                .map(RecruitBoardDocument::getId)
                .toList();

        Pageable pageable = condition.pageable();
        BooleanExpression predicate = isNotDeletedRecruitBoard()
                .and(volunteerCategoryEq(condition.category()))
                .and(regionEq(condition.region()))
                .and(admittedEq(condition.admitted()))
                .and(statusEq(condition.status()));

        List<RecruitBoardWithCenter> content = queryFactory
                .select(getRecruitBoardWithCenterConstructorExpression())
                .from(recruitBoard)
                .where(recruitBoard.id.in(boardIds)
                    .and(predicate))
                .join(center).on(recruitBoard.centerId.eq(center.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(toOrderSpecifiers(pageable.getSort()))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(recruitBoard.count())
                .from(recruitBoard)
                .join(center).on(recruitBoard.centerId.eq(center.id))
                .where(recruitBoard.id.in(boardIds)
                        .and(predicate));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<RecruitBoardDetail> findAllNearbyWithKeyword(RecruitBoardNearByCondition condition) {
        QRecruitBoard recruitBoard = QRecruitBoard.recruitBoard;
        QLocation location = QLocation.location;
        QCenter center = QCenter.center;

        List<RecruitBoardDocument> boardDocuments = getRecruitBoardDocuments(condition.keyword());

        List<Long> boardIds = boardDocuments.stream()
                .map(RecruitBoardDocument::getId)
                .toList();

        Pageable pageable = condition.pageable();

        BooleanExpression predicate = locationBetween(condition)
                .and(statusEq(condition.status()))
                .and(isNotDeletedRecruitBoard());

        List<RecruitBoardDetail> content = queryFactory
                .select(getRecruitBoardDetailConstructorExpression())
                .from(recruitBoard)
                .join(location).on(recruitBoard.locationId.eq(location.id))
                .join(center).on(recruitBoard.centerId.eq(center.id))
                .where(recruitBoard.id.in(boardIds)
                        .and(predicate))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(toOrderSpecifiers(pageable.getSort()))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(recruitBoard.count())
                .from(recruitBoard)
                .join(location).on(recruitBoard.locationId.eq(location.id))
                .join(center).on(recruitBoard.centerId.eq(center.id))
                .where(recruitBoard.id.in(boardIds)
                        .and(predicate));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
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
    public Page<CommunityBoardView> findByCommunityBoardsContaining(String keyword, Pageable pageable) {
        List<CommunityBoardDocument> boardDocuments = getCommunityBoardDocuments(keyword);

        List<Long> boardIds = boardDocuments.stream()
                .map(CommunityBoardDocument::getId)
                .toList();

        List<CommunityBoardView> content = getCommunityBoardsQuery()
                .where(communityBoard.id.in(boardIds)
                        .and(isNotDeletedCommunityBoard()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(communityBoard.count())
                .from(communityBoard)
                .join(volunteer).on(communityBoard.writerId.eq(volunteer.id))
                .where(communityBoard.id.in(boardIds)
                    .and(isNotDeletedCommunityBoard()));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public void saveCommunityBoardDocuments(List<CommunityBoard> communityBoards) {
        List<CommunityBoardDocument> communityBoardDocuments = convertCommunityBoardToDocuments(communityBoards);
        communityBoardDocumentRepository.saveAll(communityBoardDocuments);
    }

    @Override
    public void deleteCommunityBoardDocument(Long id) {
        communityBoardDocumentRepository.deleteById(id);
    }

    private List<RecruitBoardDocument> convertRecruitBoardToDocuments(List<RecruitBoard> recruitBoards) {
        List<RecruitBoardDocument> communityBoardDocuments = new ArrayList<>();

        for (RecruitBoard recruitBoard : recruitBoards) {
            RecruitBoardDocument document = RecruitBoardDocument.builder()
                    .id(recruitBoard.getId())
                    .title(recruitBoard.getTitle())
                    .content(recruitBoard.getContent())
                    .build();
            communityBoardDocuments.add(document);
        }
        return communityBoardDocuments;
    }

    private List<CommunityBoardDocument> convertCommunityBoardToDocuments(List<CommunityBoard> communityBoards) {
        List<CommunityBoardDocument> communityBoardDocuments = new ArrayList<>();

        for (CommunityBoard communityboard : communityBoards) {
            CommunityBoardDocument document = CommunityBoardDocument.builder()
                    .id(communityboard.getId())
                    .title(communityboard.getTitle())
                    .content(communityboard.getContent())
                    .build();
            communityBoardDocuments.add(document);
        }
        return communityBoardDocuments;
    }

    private JPAQuery<CommunityBoardView> getCommunityBoardsQuery() {
        return queryFactory
                .select(Projections.constructor(CommunityBoardView.class,
                        communityBoard,
                        volunteer.nickname))
                .from(communityBoard)
                .join(volunteer).on(communityBoard.writerId.eq(volunteer.id))
                .orderBy(communityBoard.createdAt.desc());
    }

    private BooleanExpression isNotDeletedRecruitBoard() {
        return recruitBoard.deleted.eq(false);
    }

    private BooleanExpression isNotDeletedCommunityBoard() {
        return communityBoard.deleted.eq(false);
    }

    private BooleanExpression volunteerCategoryEq(VolunteerCategory category) {
        return category != null ? recruitBoard.recruitmentInfo.volunteerCategory.eq(category)
                : null;
    }

    private BooleanExpression regionEq(String region) {
        return StringUtils.isNotBlank(region)
                ? recruitBoard.recruitmentInfo.region.eq(
                region) : null;
    }

    private BooleanExpression admittedEq(Boolean admitted) {
        return admitted != null ? recruitBoard.recruitmentInfo.admitted.eq(admitted)
                : null;
    }

    private BooleanExpression statusEq(RecruitStatus status) {
        return status != null ? recruitBoard.recruitStatus.eq(status) : null;
    }

    private BooleanExpression locationBetween(RecruitBoardNearByCondition condition) {
        double[] coordinates = GeoUtils.calculateMaxMinCoordinates(
                condition.latitude(),
                condition.longitude(),
                condition.radius());

        double minLatitude = coordinates[0];
        double minLongitude = coordinates[1];
        double maxLatitude = coordinates[2];
        double maxLongitude = coordinates[3];

        return location.latitude.between(minLatitude, maxLatitude)
                .and(location.longitude.between(minLongitude, maxLongitude));
    }

    private OrderSpecifier<?>[] toOrderSpecifiers(Sort sort) {
        return sort.stream()
                .map(order -> {
                    String property = order.getProperty();
                    if ("created_at".equals(property)) {
                        return order.isAscending()
                                ? recruitBoard.createdAt.asc()
                                : recruitBoard.createdAt.desc();
                    }
                    if ("volunteer_start_date_time".equals(property)) {
                        return order.isAscending()
                                ? recruitBoard.recruitmentInfo.volunteerStartDateTime.asc()
                                : recruitBoard.recruitmentInfo.volunteerStartDateTime.desc();
                    }
                    throw new IllegalStateException("Invalid sort property: " + property);
                })
                .toArray(OrderSpecifier[]::new);
    }

    private static ConstructorExpression<RecruitBoardWithCenter> getRecruitBoardWithCenterConstructorExpression() {
        return Projections.constructor(RecruitBoardWithCenter.class,
                recruitBoard, center.name);
    }

    private static ConstructorExpression<RecruitBoardDetail> getRecruitBoardDetailConstructorExpression() {
        return Projections.constructor(RecruitBoardDetail.class,
                recruitBoard, location.address, location.latitude, location.longitude, center.name);
    }
    private List<RecruitBoardDocument> getRecruitBoardDocuments(String keyword) {

        if (keyword == null || keyword.isEmpty()) {
            return recruitBoardDocumentRepository.findAll();
        }
        return recruitBoardDocumentRepository.findIdsByTitleOrContentContaining(keyword);
    }

    private List<CommunityBoardDocument> getCommunityBoardDocuments(String keyword) {

        if (keyword == null || keyword.isEmpty()) {
            return communityBoardDocumentRepository.findAll();
        }
        return communityBoardDocumentRepository.findIdsByTitleOrContentContaining(keyword);
    }
}
