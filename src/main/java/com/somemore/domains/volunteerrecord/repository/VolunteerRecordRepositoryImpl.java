package com.somemore.domains.volunteerrecord.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.domains.volunteerrecord.domain.VolunteerRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class VolunteerRecordRepositoryImpl implements VolunteerRecordRepository {

    private final JPAQueryFactory queryFactory;
    private final VolunteerRecordJpaRepository volunteerRecordJpaRepository;

    @Override
    public void save(VolunteerRecord volunteerRecord) {
        volunteerRecordJpaRepository.save(volunteerRecord);
    }

}
