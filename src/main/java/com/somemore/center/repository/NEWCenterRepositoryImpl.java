package com.somemore.center.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.center.domain.NEWCenter;
import com.somemore.center.domain.QNEWCenter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository("newCenterRepository")
@RequiredArgsConstructor
public class NEWCenterRepositoryImpl implements NEWCenterRepository {

    @Qualifier("newCenterJpaRepository")
    private final NEWCenterJpaRepository NEWCenterJpaRepository;
    private final JPAQueryFactory queryFactory;

    private static final QNEWCenter center = QNEWCenter.nEWCenter;

    @Override
    public NEWCenter save(NEWCenter center) {
        return NEWCenterJpaRepository.save(center);
    }

    @Override
    public Optional<NEWCenter> findById(UUID id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(center)
                        .where(
                                center.id.eq(id),
                                center.deleted.eq(false))
                        .fetchOne()
        );
    }

    @Override
    public Optional<NEWCenter> findByUserId(UUID userId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(center)
                        .where(
                                center.userId.eq(userId),
                                center.deleted.eq(false))
                        .fetchOne()

        );
    }
}
