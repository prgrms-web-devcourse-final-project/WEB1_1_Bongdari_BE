package com.somemore.recruitboard.repository;

import static com.somemore.location.domain.QLocation.location;
import static com.somemore.recruitboard.domain.QRecruitBoard.recruitBoard;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.center.domain.QCenter;
import com.somemore.location.domain.QLocation;
import com.somemore.location.utils.GeoUtils;
import com.somemore.recruitboard.domain.QRecruitBoard;
import com.somemore.recruitboard.domain.RecruitBoard;

import java.util.List;

import com.somemore.recruitboard.domain.RecruitStatus;
import com.somemore.recruitboard.domain.VolunteerCategory;
import com.somemore.recruitboard.repository.mapper.RecruitBoardDetail;
import com.somemore.recruitboard.repository.mapper.RecruitBoardWithCenter;
import com.somemore.recruitboard.repository.mapper.RecruitBoardWithLocation;
import com.somemore.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.recruitboard.dto.condition.RecruitBoardSearchCondition;

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
    private final JPAQueryFactory queryFactory;

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
        QRecruitBoard recruitBoard = QRecruitBoard.recruitBoard;

        RecruitBoard result = queryFactory
                .selectFrom(recruitBoard)
                .where(isNotDeleted().and(idEq(id)))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<Long> findNotCompletedIdsByCenterId(UUID centerId) {
        QRecruitBoard recruitBoard = QRecruitBoard.recruitBoard;

        return queryFactory
                .select(recruitBoard.id)
                .from(recruitBoard)
                .where(
                        recruitBoard.centerId.eq(centerId)
                                .and(isNotCompleted())
                                .and(isNotDeleted())
                )
                .fetch();
    }

    @Override
    public Optional<RecruitBoardWithLocation> findWithLocationById(Long id) {
        QRecruitBoard recruitBoard = QRecruitBoard.recruitBoard;
        QLocation location = QLocation.location;

        return Optional.ofNullable(
                queryFactory.select(
                                getRecruitBoardWithLocationConstructorExpression(recruitBoard, location))
                        .from(recruitBoard)
                        .join(location).on(recruitBoard.locationId.eq(location.id))
                        .where(isNotDeleted().and(idEq(id)))
                        .fetchOne());
    }

    @Override
    public Page<RecruitBoardWithCenter> findAllWithCenter(RecruitBoardSearchCondition condition) {
        QRecruitBoard recruitBoard = QRecruitBoard.recruitBoard;
        QCenter center = QCenter.center;

        Pageable pageable = condition.pageable();
        BooleanExpression predicate = isNotDeleted()
                .and(keywordEq(condition.keyword()))
                .and(volunteerCategoryEq(condition.category()))
                .and(regionEq(condition.region()))
                .and(admittedEq(condition.admitted()))
                .and(statusEq(condition.status()));

        List<RecruitBoardWithCenter> content = queryFactory
                .select(getRecruitBoardWithCenterConstructorExpression(recruitBoard, center))
                .from(recruitBoard)
                .where(predicate)
                .join(center).on(recruitBoard.centerId.eq(center.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(toOrderSpecifiers(pageable.getSort()))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(recruitBoard.count())
                .from(recruitBoard)
                .where(predicate);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<RecruitBoardDetail> findAllNearby(RecruitBoardNearByCondition condition) {
        QRecruitBoard recruitBoard = QRecruitBoard.recruitBoard;
        QLocation location = QLocation.location;
        QCenter center = QCenter.center;

        Pageable pageable = condition.pageable();

        BooleanExpression predicate = isNotDeleted()
                .and(locationBetween(condition))
                .and(keywordEq(condition.keyword()));

        List<RecruitBoardDetail> content = queryFactory
                .select(getRecruitBoardDetailConstructorExpression(recruitBoard, location, center))
                .from(recruitBoard)
                .join(location).on(recruitBoard.locationId.eq(location.id))
                .join(center).on(recruitBoard.centerId.eq(center.id))
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(toOrderSpecifiers(pageable.getSort()))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(recruitBoard.count())
                .from(recruitBoard)
                .join(location).on(recruitBoard.locationId.eq(location.id))
                .join(center).on(recruitBoard.centerId.eq(center.id))
                .where(predicate);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<RecruitBoard> findAllByCenterId(UUID centerId,
                                                RecruitBoardSearchCondition condition) {
        QRecruitBoard recruitBoard = QRecruitBoard.recruitBoard;

        Pageable pageable = condition.pageable();
        BooleanExpression predicate = isNotDeleted()
                .and(centerIdEq(centerId))
                .and(keywordEq(condition.keyword()))
                .and(volunteerCategoryEq(condition.category()))
                .and(regionEq(condition.region()))
                .and(admittedEq(condition.admitted()))
                .and(statusEq(condition.status()));

        List<RecruitBoard> content = queryFactory
                .selectFrom(recruitBoard)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(toOrderSpecifiers(pageable.getSort()))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(recruitBoard.count())
                .from(recruitBoard)
                .where(predicate);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

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
        QRecruitBoard recruitBoard = QRecruitBoard.recruitBoard;
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
        QRecruitBoard recruitBoard = QRecruitBoard.recruitBoard;

        return sort.stream()
                .map(order -> {
                    String property = order.getProperty();

                    if ("created_at".equals(property)) {
                        return order.isAscending()
                                ? recruitBoard.createdAt.asc()
                                : recruitBoard.createdAt.desc();
                    } else if ("volunteer_start_date_time".equals(property)) {
                        return order.isAscending()
                                ? recruitBoard.recruitmentInfo.volunteerStartDateTime.asc()
                                : recruitBoard.recruitmentInfo.volunteerStartDateTime.desc();
                    } else {
                        throw new IllegalStateException("Invalid sort property: " + property);
                    }
                })
                .toArray(OrderSpecifier[]::new);
    }

    private static ConstructorExpression<RecruitBoardWithCenter> getRecruitBoardWithCenterConstructorExpression(
            QRecruitBoard recruitBoard, QCenter center) {
        return Projections.constructor(RecruitBoardWithCenter.class,
                recruitBoard, center.name);
    }

    private static ConstructorExpression<RecruitBoardWithLocation> getRecruitBoardWithLocationConstructorExpression(
            QRecruitBoard recruitBoard, QLocation location) {
        return Projections.constructor(RecruitBoardWithLocation.class,
                recruitBoard, location.address, location.latitude, location.longitude);
    }

    private static ConstructorExpression<RecruitBoardDetail> getRecruitBoardDetailConstructorExpression(
            QRecruitBoard recruitBoard, QLocation location, QCenter center) {
        return Projections.constructor(RecruitBoardDetail.class,
                recruitBoard, location.address, location.latitude, location.longitude, center.name);
    }

}
