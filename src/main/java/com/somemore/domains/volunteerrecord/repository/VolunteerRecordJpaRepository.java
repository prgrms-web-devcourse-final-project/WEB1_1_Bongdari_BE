package com.somemore.domains.volunteerrecord.repository;

import com.somemore.domains.volunteerrecord.domain.VolunteerRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VolunteerRecordJpaRepository extends JpaRepository<VolunteerRecord, Long> {

    @Query(value = """
        SELECT volunteerId, totalHours, ranking
        FROM (
            SELECT 
                vr.volunteer_id AS volunteerId, 
                SUM(vr.volunteer_hours) AS totalHours,
                DENSE_RANK() OVER (ORDER BY SUM(vr.volunteer_hours) DESC) AS ranking
            FROM volunteer_record vr
            GROUP BY vr.volunteer_id
        ) ranked
        WHERE ranking <= 4
        """, nativeQuery = true)
    List<Object[]> findTotalTopRankingWithTies();

    @Query(value = """
        SELECT volunteerId, totalHours, ranking
        FROM (
            SELECT 
                vr.volunteer_id AS volunteerId, 
                SUM(vr.volunteer_hours) AS totalHours,
                DENSE_RANK() OVER (ORDER BY SUM(vr.volunteer_hours) DESC) AS ranking
            FROM volunteer_record vr
            WHERE YEARWEEK(vr.volunteer_date, 1) = YEARWEEK(CURDATE(), 1)
            GROUP BY vr.volunteer_id
        ) ranked
        WHERE ranking <= 4
        """, nativeQuery = true)
    List<Object[]> findWeeklyTopRankingWithTies();

    @Query(value = """
        SELECT volunteerId, totalHours, ranking
        FROM (
            SELECT 
                vr.volunteer_id AS volunteerId, 
                SUM(vr.volunteer_hours) AS totalHours,
                DENSE_RANK() OVER (ORDER BY SUM(vr.volunteer_hours) DESC) AS ranking
            FROM volunteer_record vr
            WHERE MONTH(vr.volunteer_date) = MONTH(CURDATE())
              AND YEAR(vr.volunteer_date) = YEAR(CURDATE())
            GROUP BY vr.volunteer_id
        ) ranked
        WHERE ranking <= 4
        """, nativeQuery = true)
    List<Object[]> findMonthlyTopRankingWithTies();
}
