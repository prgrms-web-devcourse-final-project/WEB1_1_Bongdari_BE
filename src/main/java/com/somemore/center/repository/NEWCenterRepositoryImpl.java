package com.somemore.center.repository;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.center.domain.NEWCenter;
import com.somemore.center.domain.QNEWCenter;
import com.somemore.center.repository.record.CenterOverviewInfo;
import com.somemore.center.repository.record.CenterProfileDto;
import com.somemore.user.domain.QUserCommonAttribute;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository("newCenterRepository")
@RequiredArgsConstructor
public class NEWCenterRepositoryImpl implements NEWCenterRepository {

    private final NEWCenterJpaRepository centerJpaRepository;
    private final JPAQueryFactory queryFactory;

    private static final QNEWCenter center = QNEWCenter.nEWCenter;
    private static final QUserCommonAttribute userCommonAttribute = QUserCommonAttribute.userCommonAttribute;

    @Override
    public NEWCenter save(NEWCenter center) {
        return centerJpaRepository.save(center);
    }

    @Override
    public Optional<NEWCenter> findById(UUID id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(center)
                        .where(
                                idEq(id),
                                isNotDeleted()
                        )
                        .fetchOne()
        );
    }

    @Override
    public Optional<NEWCenter> findByUserId(UUID userId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(center)
                        .where(
                                center.userId.eq(userId),
                                isNotDeleted()
                        )
                        .fetchOne()

        );
    }

    @Override
    public Optional<CenterProfileDto> findCenterProfileById(UUID centerId) {
        return Optional.ofNullable(
                queryFactory.select(Projections.constructor(CenterProfileDto.class,
                                center.id,
                                center.userId,
                                center.homepageUrl))
                        .from(center)
                        .where(
                                idEq(centerId),
                                isNotDeleted()
                        )
                        .fetchOne()
        );
    }

    @Override
    public boolean existsById(UUID id) {
        return queryFactory
                .selectOne()
                .from(center)
                .where(
                        idEq(id),
                        isNotDeleted()
                )
                .fetchFirst() != null;
    }

    @Override
    public List<CenterOverviewInfo> findOverviewInfosByIds(List<UUID> ids) {
        return queryFactory
                .select(Projections.constructor(
                        CenterOverviewInfo.class,
                        center.id,
                        userCommonAttribute.name,
                        userCommonAttribute.imgUrl
                ))
                .from(center)
                .join(userCommonAttribute)
                .on(userCommonAttribute.userId.eq(center.userId))
                .where(
                        idIn(ids),
                        isNotDeleted()
                )
                .fetch();
    }

    @Override
    public Optional<String> findNameById(UUID id) {
        return findDynamicFieldByCenterId(id, userCommonAttribute.name);
    }

    private static BooleanExpression idIn(List<UUID> ids) {
        return center.id.in(ids);
    }

    private BooleanExpression idEq(UUID id) {
        return center.id.eq(id);
    }

    private BooleanExpression isNotDeleted() {
        return center.deleted.isFalse();
    }

    private <T> Optional<T> findDynamicFieldByCenterId(UUID id, Path<T> field) {
        return Optional.ofNullable(
                queryFactory
                        .select(field)
                        .from(center)
                        .join(userCommonAttribute)
                        .on(userCommonAttribute.userId.eq(center.userId))
                        .where(
                                center.id.eq(id),
                                isNotDeleted()
                        )
                        .fetchOne()
        );
    }
}
