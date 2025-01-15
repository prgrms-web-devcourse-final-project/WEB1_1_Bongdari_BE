package com.somemore.volunteer.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.volunteer.domain.NEWVolunteer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository("newVolunteerRepository")
@RequiredArgsConstructor
public class NEWVolunteerRepositoryImpl implements NEWVolunteerRepository {

    @Qualifier("newVolunteerJpaRepository")
    private final NEWVolunteerJpaRepository NEWVolunteerJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public NEWVolunteer save(NEWVolunteer volunteer) {
        return NEWVolunteerJpaRepository.save(volunteer);
    }
}
