package com.somemore.domains.volunteerrecord.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.domains.volunteerrecord.domain.QVolunteerRecord;
import com.somemore.domains.volunteerrecord.domain.VolunteerRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class VolunteerRecordRepositoryImpl implements VolunteerRecordRepository {

    private final JPAQueryFactory queryFactory;
    private final VolunteerRecordJpaRepository volunteerRecordJpaRepository;

    private static final QVolunteerRecord volunteerRecord = QVolunteerRecord.volunteerRecord;

    @Override
    public void save(VolunteerRecord volunteerRecord) {
        volunteerRecordJpaRepository.save(volunteerRecord);
    }

    @Override
    public List<Object[]> findTotalTopRankingWithTies() {
        return volunteerRecordJpaRepository.findTotalTopRankingWithTies();
    }

    @Override
    public List<Object[]> findWeeklyTopRankingWithTies() {
        return volunteerRecordJpaRepository.findWeeklyTopRankingWithTies();
    }

    @Override
    public List<Object[]> findMonthlyTopRankingWithTies() {
        return volunteerRecordJpaRepository.findMonthlyTopRankingWithTies();
    }


}
