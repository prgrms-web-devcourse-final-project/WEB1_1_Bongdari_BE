package com.somemore.domains.center.repository.center;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.domains.center.domain.Center;
import com.somemore.domains.center.domain.QCenter;
import com.somemore.domains.center.repository.mapper.CenterOverviewInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class CenterRepositoryImpl implements CenterRepository {

    private final JPAQueryFactory queryFactory;
    private final CenterJpaRepository centerJpaRepository;

    private static final QCenter center = QCenter.center;

    @Override
    public Center save(Center center) {
        return centerJpaRepository.save(center);
    }

    @Override
    public boolean existsById(UUID id) {
        return centerJpaRepository.existsByIdAndDeletedIsFalse(id);
    }

    @Override
    public Optional<Center> findCenterById(UUID id) {

        return Optional.ofNullable(
                queryFactory
                        .selectFrom(center)
                        .where(center.id.eq(id)
                                .and(isNotDeleted()))
                        .fetchOne()
        );
    }

    @Override
    public List<CenterOverviewInfo> findCenterOverviewsByIds(List<UUID> ids) {

        return queryFactory
                .select(Projections.constructor(
                        CenterOverviewInfo.class,
                        center.id,
                        center.name,
                        center.imgUrl
                ))
                .from(center)
                .where(center.id.in(ids)
                        .and(isNotDeleted())
                )
                .fetch();
    }

    @Override
    public void deleteAllInBatch() {
        centerJpaRepository.deleteAllInBatch();
    }

    private static BooleanExpression isNotDeleted() {
        return center.deleted.isFalse();
    }
}
