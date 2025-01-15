package com.somemore.center.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.center.domain.NEWCenter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository("newCenterRepository")
@RequiredArgsConstructor
public class NEWImplCenterRepository implements NEWCenterRepository {

    @Qualifier("newCenterJpaRepository")
    private final NEWCenterJpaRepository NEWCenterJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public NEWCenter save(NEWCenter center) {
        return NEWCenterJpaRepository.save(center);
    }
}
