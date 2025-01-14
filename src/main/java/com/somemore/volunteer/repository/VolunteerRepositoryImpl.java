package com.somemore.volunteer.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.volunteer.domain.Volunteer_NEW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VolunteerRepositoryImpl implements VolunteerRepository {

    private final VolunteerJpaRepository volunteerJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Volunteer_NEW save(Volunteer_NEW volunteer) {
        return volunteerJpaRepository.save(volunteer);
    }
}
