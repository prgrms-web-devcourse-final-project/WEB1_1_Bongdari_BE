package com.somemore.center.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.center.domain.Center;
import com.somemore.center.domain.QCenter;
import com.somemore.center.repository.mapper.CenterOverviewInfo;
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

    @Override
    public Center save(Center center) {
        return centerJpaRepository.save(center);
    }

    @Override
    public boolean existsById(UUID id) {
        return centerJpaRepository.existsById(id);
    }

    @Override
    public Optional<Center> findCenterById(UUID id) {
        return centerJpaRepository.findCenterById(id);
    }

    @Override
    public List<CenterOverviewInfo> findCenterOverviewsByIds(List<UUID> ids) {
        QCenter center = QCenter.center;

        return queryFactory
                .select(Projections.constructor(
                        CenterOverviewInfo.class,
                        center.id,
                        center.name,
                        center.imgUrl
                ))
                .from(center)
                .where(center.id.in(ids)
                        .and(center.deleted.eq(false)))
                .fetch();
    }

    @Override
    public void deleteAllInBatch() {
        centerJpaRepository.deleteAllInBatch();
    }
}
