package com.somemore.center.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.center.domain.Center;
import com.somemore.center.domain.QCenter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class CenterRepositoryImpl implements CenterRepository {

    private final CenterJpaRepository centerJpaRepository;
    private final JPAQueryFactory queryFactory;

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
    public String findNameById(UUID id) {
        QCenter center = QCenter.center;

        return queryFactory
                .select(center.name)
                .from(center)
                .where(center.id.eq(id))
                .fetchOne();
    }

    @Override
    public void deleteAllInBatch() {
        centerJpaRepository.deleteAllInBatch();
    }
}
