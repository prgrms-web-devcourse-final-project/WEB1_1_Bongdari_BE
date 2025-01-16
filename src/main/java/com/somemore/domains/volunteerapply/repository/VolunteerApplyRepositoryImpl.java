package com.somemore.domains.volunteerapply.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.domains.volunteerapply.domain.ApplyStatus;
import com.somemore.domains.volunteerapply.domain.QVolunteerApply;
import com.somemore.domains.volunteerapply.domain.VolunteerApply;
import com.somemore.domains.volunteerapply.dto.condition.VolunteerApplySearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class VolunteerApplyRepositoryImpl implements VolunteerApplyRepository {

    private final VolunteerApplyJpaRepository volunteerApplyJpaRepository;
    private final JPAQueryFactory queryFactory;

    private static final QVolunteerApply volunteerApply = QVolunteerApply.volunteerApply;

    @Override
    public VolunteerApply save(VolunteerApply volunteerApply) {
        return volunteerApplyJpaRepository.save(volunteerApply);
    }

    @Override
    public List<VolunteerApply> saveAll(List<VolunteerApply> volunteerApplies) {
        return volunteerApplyJpaRepository.saveAll(volunteerApplies);
    }

    @Override
    public Optional<VolunteerApply> findById(Long id) {
        return findOne(volunteerApply.id.eq(id));
    }

    @Override
    public List<UUID> findVolunteerIdsByRecruitIds(List<Long> recruitIds) {

        BooleanExpression exp = volunteerApply.recruitBoardId
                .in(recruitIds)
                .and(isNotDeleted());

        return queryFactory
                .select(volunteerApply.volunteerId)
                .from(volunteerApply)
                .where(exp)
                .fetch();
    }

    @Override
    public Page<VolunteerApply> findAllByRecruitId(Long recruitId, Pageable pageable) {

        BooleanExpression exp = recruitIdEq(recruitId)
                .and(isNotDeleted());

        List<VolunteerApply> content = queryFactory
                .selectFrom(volunteerApply)
                .where(exp)
                .orderBy(toOrderSpecifiers(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(
                content,
                pageable,
                getCount(exp)
        );
    }

    @Override
    public List<VolunteerApply> findAllByRecruitId(Long recruitId) {
        BooleanExpression exp = recruitIdEq(recruitId)
                .and(isNotDeleted());

        return queryFactory
                .select(volunteerApply)
                .from(volunteerApply)
                .where(exp)
                .fetch();
    }

    @Override
    public Page<VolunteerApply> findAllByRecruitId(Long recruitId,
                                                   VolunteerApplySearchCondition condition) {

        BooleanExpression exp = recruitIdEq(recruitId)
                .and(attendedEq(condition.attended()))
                .and(statusEq(condition.status()))
                .and(isNotDeleted());

        Pageable pageable = condition.pageable();

        List<VolunteerApply> content = queryFactory
                .selectFrom(volunteerApply)
                .where(exp)
                .orderBy(toOrderSpecifiers(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(
                content,
                pageable,
                getCount(exp)
        );
    }

    @Override
    public Page<VolunteerApply> findAllByVolunteerId(UUID volunteerId,
                                                     VolunteerApplySearchCondition condition) {

        BooleanExpression exp = volunteerIdEq(volunteerId)
                .and(attendedEq(condition.attended()))
                .and(statusEq(condition.status()))
                .and(isNotDeleted());

        Pageable pageable = condition.pageable();

        List<VolunteerApply> content = queryFactory
                .selectFrom(volunteerApply)
                .where(exp)
                .orderBy(toOrderSpecifiers(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(
                content,
                pageable,
                getCount(exp)
        );
    }

    @Override
    public List<VolunteerApply> findAllByIds(List<Long> ids) {
        BooleanExpression exp = volunteerApply.id.in(ids)
                .and(isNotDeleted());

        return queryFactory
                .selectFrom(volunteerApply)
                .where(exp)
                .fetch();
    }

    @Override
    public Optional<VolunteerApply> findByRecruitIdAndVolunteerId(Long recruitId,
                                                                  UUID volunteerId) {
        BooleanExpression exp = recruitIdEq(recruitId)
                .and(volunteerApply.volunteerId.eq(volunteerId));
        return findOne(exp);
    }

    @Override
    public boolean existsByRecruitIdAndVolunteerId(Long recruitId, UUID volunteerId) {
        return queryFactory
                .selectFrom(volunteerApply)
                .where(recruitIdEq(recruitId)
                        .and(volunteerIdEq(volunteerId))
                        .and(isNotDeleted()))
                .fetchFirst() != null;
    }

    private Long getCount(BooleanExpression exp) {
        return queryFactory
                .select(volunteerApply.count())
                .from(volunteerApply)
                .where(exp)
                .fetchOne();
    }

    private Optional<VolunteerApply> findOne(BooleanExpression condition) {

        return Optional.ofNullable(
                queryFactory
                        .selectFrom(volunteerApply)
                        .where(
                                condition,
                                isNotDeleted()
                        )
                        .fetchOne()
        );
    }

    private static BooleanExpression recruitIdEq(Long recruitId) {
        return volunteerApply.recruitBoardId.eq(recruitId);
    }

    private static BooleanExpression volunteerIdEq(UUID volunteerId) {
        return volunteerApply.volunteerId.eq(volunteerId);
    }

    private BooleanExpression attendedEq(Boolean attended) {
        return attended != null ? volunteerApply.attended.eq(attended) : null;
    }

    private BooleanExpression statusEq(ApplyStatus status) {
        return status != null ? volunteerApply.status.eq(status) : null;
    }

    private BooleanExpression isNotDeleted() {
        return volunteerApply.deleted.isFalse();
    }

    private OrderSpecifier<?>[] toOrderSpecifiers(Sort sort) {

        return sort.stream()
                .map(order -> {
                    String property = order.getProperty();

                    if ("created_at".equals(property)) {
                        return order.isAscending()
                                ? volunteerApply.createdAt.asc()
                                : volunteerApply.createdAt.desc();
                    } else {
                        throw new IllegalStateException("Invalid sort property: " + property);
                    }
                })
                .toArray(OrderSpecifier[]::new);
    }
}
