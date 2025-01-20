package com.somemore.domains.volunteerrecord.repository;

import com.somemore.domains.volunteerrecord.domain.VolunteerRecord;

import java.util.List;

public interface VolunteerRecordRepository {
    void save(VolunteerRecord volunteerRecord);
    List<Object[]> findTotalTopRankingWithTies();
    List<Object[]> findWeeklyTopRankingWithTies();
    List<Object[]> findMonthlyTopRankingWithTies();

}
