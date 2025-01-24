package com.somemore.center.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.center.domain.NEWCenter;
import com.somemore.center.domain.QNEWCenter;
import com.somemore.center.repository.record.CenterProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository("newCenterRepository")
@RequiredArgsConstructor
public class NEWCenterRepositoryImpl implements NEWCenterRepository {

    private final NEWCenterJpaRepository centerJpaRepository;
    private final JPAQueryFactory queryFactory;

    private static final QNEWCenter center = QNEWCenter.nEWCenter;

    @Override
    public NEWCenter save(NEWCenter center) {
        return centerJpaRepository.save(center);
    }

    @Override
    public Optional<NEWCenter> findById(UUID id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(center)
                        .where(
                                center.id.eq(id),
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
                                center.id.eq(centerId),
                                isNotDeleted()
                        )
                        .fetchOne()
        );
    }

    private BooleanExpression isNotDeleted() {
        return center.deleted.isFalse();
    }

}
