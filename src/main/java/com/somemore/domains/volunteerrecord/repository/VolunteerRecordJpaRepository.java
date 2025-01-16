package com.somemore.domains.volunteerrecord.repository;

import com.somemore.domains.volunteerrecord.domain.VolunteerRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VolunteerRecordJpaRepository extends JpaRepository<VolunteerRecord, Long> {

    @Query("""
        SELECT ranked.volunteerId, ranked.totalHours, ranked.ranking
        FROM (
            SELECT 
                vr.volunteerId AS volunteerId, 
                SUM(vr.volunteerHours) AS totalHours,
                DENSE_RANK() OVER (ORDER BY SUM(vr.volunteerHours) DESC) AS ranking
            FROM VolunteerRecord vr
            GROUP BY vr.volunteerId
        ) ranked
        WHERE ranked.ranking <= 4
    """)
    List<Object[]> findTotalTopRankingWithTies();

    @Query("""
        SELECT ranked.volunteerId, ranked.totalHours, ranked.ranking
        FROM (
            SELECT 
                vr.volunteerId AS volunteerId, 
                SUM(vr.volunteerHours) AS totalHours,
                DENSE_RANK() OVER (ORDER BY SUM(vr.volunteerHours) DESC) AS ranking
            FROM VolunteerRecord vr
            WHERE YEARWEEK(vr.volunteerDate, 1) = YEARWEEK(CURDATE(), 1)
            GROUP BY vr.volunteerId
        ) ranked
        WHERE ranked.ranking <= 4
    """)
    List<Object[]> findWeeklyTopRankingWithTies();

    @Query("""
        SELECT ranked.volunteerId, ranked.totalHours, ranked.ranking
        FROM (
            SELECT 
                vr.volunteerId AS volunteerId, 
                SUM(vr.volunteerHours) AS totalHours,
                DENSE_RANK() OVER (ORDER BY SUM(vr.volunteerHours) DESC) AS ranking
            FROM VolunteerRecord vr
            WHERE MONTH(vr.volunteerDate) = MONTH(CURDATE())
              AND YEAR(vr.volunteerDate) = YEAR(CURDATE())
            GROUP BY vr.volunteerId
        ) ranked
        WHERE ranked.ranking <= 4
    """)
    List<Object[]> findMonthlyTopRankingWithTies();

}
