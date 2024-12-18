package com.somemore.domains.recruitboard.repository;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.domains.center.domain.QCenter;
import com.somemore.domains.location.domain.QLocation;
import com.somemore.domains.recruitboard.domain.QRecruitBoard;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.domains.recruitboard.repository.mapper.RecruitBoardDetail;
import com.somemore.domains.recruitboard.repository.mapper.RecruitBoardWithCenter;
import com.somemore.domains.recruitboard.repository.mapper.RecruitBoardWithLocation;
import com.somemore.domains.location.utils.GeoUtils;
import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.domain.RecruitStatus;
import com.somemore.domains.recruitboard.domain.VolunteerCategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RecruitBoardRepositoryImpl implements RecruitBoardRepository {

    private final RecruitBoardJpaRepository recruitBoardJpaRepository;
    //    private final RecruitBoardDocumentRepository documentRepository;
    private final JPAQueryFactory queryFactory;

    private static final QRecruitBoard recruitBoard = QRecruitBoard.recruitBoard;
    private static final QLocation location = QLocation.location;
    private static final QCenter center = QCenter.center;

    @Override
    public RecruitBoard save(RecruitBoard recruitBoard) {
        return recruitBoardJpaRepository.save(recruitBoard);
    }

    @Override
    public List<RecruitBoard> saveAll(List<RecruitBoard> recruitBoards) {
        return recruitBoardJpaRepository.saveAll(recruitBoards);
    }

    @Override
    public Optional<RecruitBoard> findById(Long id) {

        BooleanExpression exp = idEq(id)
                .and(isNotDeleted());

        RecruitBoard result = queryFactory
                .selectFrom(recruitBoard)
                .where(exp)
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<Long> findNotCompletedIdsByCenterId(UUID centerId) {

        BooleanExpression exp = centerIdEq(centerId)
                .and(isNotCompleted())
                .and(isNotDeleted());

        return queryFactory
                .select(recruitBoard.id)
                .from(recruitBoard)
                .where(exp)
                .fetch();
    }

    @Override
    public List<RecruitBoard> findAllByIds(List<Long> ids) {
        BooleanExpression exp = recruitBoard.id.in(ids)
                .and(isNotDeleted());

        return queryFactory
                .selectFrom(recruitBoard)
                .where(exp)
                .fetch();
    }

    @Override
    public Optional<RecruitBoardWithLocation> findWithLocationById(Long id) {

        BooleanExpression exp = idEq(id)
                .and(isNotDeleted());

        RecruitBoardWithLocation result = queryFactory
                .select(getRecruitBoardWithLocationConstructorExpression())
                .from(recruitBoard)
                .join(location)
                .on(recruitBoard.locationId.eq(location.id))
                .where(exp)
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Page<RecruitBoardWithCenter> findAllWithCenter(RecruitBoardSearchCondition condition) {

        BooleanExpression exp = isNotDeleted()
                .and(keywordEq(condition.keyword()))
                .and(volunteerCategoryEq(condition.category()))
                .and(regionEq(condition.region()))
                .and(admittedEq(condition.admitted()))
                .and(statusEq(condition.status()));

        Pageable pageable = condition.pageable();

        List<RecruitBoardWithCenter> content = queryFactory
                .select(getRecruitBoardWithCenterConstructorExpression())
                .from(recruitBoard)
                .where(exp)
                .join(center).on(recruitBoard.centerId.eq(center.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(toOrderSpecifiers(pageable.getSort()))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(recruitBoard.count())
                .from(recruitBoard)
                .where(exp)
                .join(center).on(recruitBoard.centerId.eq(center.id));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<RecruitBoardDetail> findAllNearby(RecruitBoardNearByCondition condition) {

        BooleanExpression exp = locationBetween(condition)
                .and(keywordEq(condition.keyword()))
                .and(statusEq(condition.status()))
                .and(isNotDeleted());

        Pageable pageable = condition.pageable();

        List<RecruitBoardDetail> content = queryFactory
                .select(getRecruitBoardDetailConstructorExpression())
                .from(recruitBoard)
                .join(location).on(recruitBoard.locationId.eq(location.id))
                .join(center).on(recruitBoard.centerId.eq(center.id))
                .where(exp)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(toOrderSpecifiers(pageable.getSort()))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(recruitBoard.count())
                .from(recruitBoard)
                .join(location).on(recruitBoard.locationId.eq(location.id))
                .join(center).on(recruitBoard.centerId.eq(center.id))
                .where(exp);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

//    @Override
//    public Page<RecruitBoardDetail> findAllNearbyWithKeyword(RecruitBoardNearByCondition condition) {
//        QRecruitBoard recruitBoard = QRecruitBoard.recruitBoard;
//        QLocation location = QLocation.location;
//        QCenter center = QCenter.center;
//
//        List<RecruitBoardDocument> boardDocuments = getBoardDocuments(condition.keyword());
//
//        List<Long> boardIds = boardDocuments.stream()
//                .map(RecruitBoardDocument::getId)
//                .toList();
//
//        Pageable pageable = condition.pageable();
//
//        BooleanExpression predicate = locationBetween(condition)
//                .and(statusEq(condition.status()))
//                .and(isNotDeleted());
//
//        List<RecruitBoardDetail> content = queryFactory
//                .select(getRecruitBoardDetailConstructorExpression(recruitBoard, location, center))
//                .from(recruitBoard)
//                .join(location).on(recruitBoard.locationId.eq(location.id))
//                .join(center).on(recruitBoard.centerId.eq(center.id))
//                .where(recruitBoard.id.in(boardIds)
//                        .and(predicate))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .orderBy(toOrderSpecifiers(pageable.getSort()))
//                .fetch();
//
//        JPAQuery<Long> countQuery = queryFactory
//                .select(recruitBoard.count())
//                .from(recruitBoard)
//                .join(location).on(recruitBoard.locationId.eq(location.id))
//                .join(center).on(recruitBoard.centerId.eq(center.id))
//                .where(recruitBoard.id.in(boardIds)
//                        .and(predicate));
//
//        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
//    }

    @Override
    public Page<RecruitBoard> findAllByCenterId(UUID centerId,
            RecruitBoardSearchCondition condition) {

        BooleanExpression exp = centerIdEq(centerId)
                .and(keywordEq(condition.keyword()))
                .and(volunteerCategoryEq(condition.category()))
                .and(regionEq(condition.region()))
                .and(admittedEq(condition.admitted()))
                .and(statusEq(condition.status()))
                .and(isNotDeleted());

        Pageable pageable = condition.pageable();

        List<RecruitBoard> content = queryFactory
                .selectFrom(recruitBoard)
                .where(exp)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(toOrderSpecifiers(pageable.getSort()))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(recruitBoard.count())
                .from(recruitBoard)
                .where(exp);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

//    @Override
//    public Page<RecruitBoardWithCenter> findByRecruitBoardsContaining(RecruitBoardSearchCondition condition) {
//        QRecruitBoard recruitBoard = QRecruitBoard.recruitBoard;
//        QCenter center = QCenter.center;
//
//
//        List<RecruitBoardDocument> boardDocuments = getBoardDocuments(condition.keyword());
//
//        List<Long> boardIds = boardDocuments.stream()
//                .map(RecruitBoardDocument::getId)
//                .toList();
//
//        Pageable pageable = condition.pageable();
//        BooleanExpression predicate = isNotDeleted()
//                .and(volunteerCategoryEq(condition.category()))
//                .and(regionEq(condition.region()))
//                .and(admittedEq(condition.admitted()))
//                .and(statusEq(condition.status()));
//
//        List<RecruitBoardWithCenter> content = queryFactory
//                .select(getRecruitBoardWithCenterConstructorExpression(recruitBoard, center))
//                .from(recruitBoard)
//                .where(recruitBoard.id.in(boardIds)
//                    .and(predicate))
//                .join(center).on(recruitBoard.centerId.eq(center.id))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .orderBy(toOrderSpecifiers(pageable.getSort()))
//                .fetch();
//
//        JPAQuery<Long> countQuery = queryFactory
//                .select(recruitBoard.count())
//                .from(recruitBoard)
//                .join(center).on(recruitBoard.centerId.eq(center.id))
//                .where(recruitBoard.id.in(boardIds)
//                        .and(predicate));
//
//        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
//    }
//
//    @Override
//    public void saveDocuments(List<RecruitBoard> recruitBoards) {
//        List<RecruitBoardDocument> recruitBoardDocuments = convertEntityToDocuments(recruitBoards);
//        documentRepository.saveAll(recruitBoardDocuments);
//    }

    @Override
    public List<RecruitBoard> findAll() {
        return recruitBoardJpaRepository.findAll();
    }

//    @Override
//    public void deleteDocument(Long id) {
//        documentRepository.deleteById(id);
//    }

    private static BooleanExpression idEq(Long id) {
        return recruitBoard.id.eq(id);
    }

    private static BooleanExpression centerIdEq(UUID centerId) {
        return recruitBoard.centerId.eq(centerId);
    }

    private BooleanExpression isNotDeleted() {
        return recruitBoard.deleted.eq(false);
    }

    private BooleanExpression isNotCompleted() {
        return recruitBoard.recruitStatus.in(RecruitStatus.RECRUITING, RecruitStatus.CLOSED);
    }

    private BooleanExpression keywordEq(String keyword) {
        return StringUtils.isNotBlank(keyword)
                ? recruitBoard.title.containsIgnoreCase(
                keyword) : null;
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

    private static ConstructorExpression<RecruitBoardWithLocation> getRecruitBoardWithLocationConstructorExpression() {
        return Projections.constructor(RecruitBoardWithLocation.class,
                recruitBoard, location.address, location.latitude, location.longitude);
    }

    private static ConstructorExpression<RecruitBoardDetail> getRecruitBoardDetailConstructorExpression() {
        return Projections.constructor(RecruitBoardDetail.class,
                recruitBoard, location.address, location.latitude, location.longitude, center.name);
    }

//    private List<RecruitBoardDocument> convertEntityToDocuments(List<RecruitBoard> recruitBoards) {
//        List<RecruitBoardDocument> communityBoardDocuments = new ArrayList<>();
//
//        for (RecruitBoard recruitBoard : recruitBoards) {
//            RecruitBoardDocument document = RecruitBoardDocument.builder()
//                    .id(recruitBoard.getId())
//                    .title(recruitBoard.getTitle())
//                    .content(recruitBoard.getContent())
//                    .build();
//            communityBoardDocuments.add(document);
//        }
//        return communityBoardDocuments;
//    }
//
//    private List<RecruitBoardDocument> getBoardDocuments(String keyword) {
//
//        if (keyword == null || keyword.isEmpty()) {
//            return documentRepository.findAll();
//        }
//        return documentRepository.findIdsByTitleOrContentContaining(keyword);
//    }
}
