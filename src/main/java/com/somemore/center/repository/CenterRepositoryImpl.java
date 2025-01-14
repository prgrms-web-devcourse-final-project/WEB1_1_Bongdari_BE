package com.somemore.center.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.center.domain.Center_NEW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CenterRepositoryImpl implements CenterRepository {

    private final CenterJpaRepository centerJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Center_NEW save(Center_NEW center) {
        return center;
    }
}
