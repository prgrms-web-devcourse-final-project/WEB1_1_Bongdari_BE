package com.somemore.domains.volunteerrecord.repository;

import com.somemore.domains.volunteerrecord.domain.VolunteerRecord;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
class VolunteerRecordRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private VolunteerRecordRepository volunteerRecordrepository;

    @DisplayName("전체 봉사 시간으로 4위까지의 랭킹을 반환할 수 있다. 동점자를 같이 반환한다. (Repository)")
    @Test
    void findTotalTopRankingWithTies() {

        //given
        volunteerRecordrepository.save(VolunteerRecord.create(UUID.randomUUID(), "봉사1", LocalDate.of(2025, 1, 10), 100));
        volunteerRecordrepository.save(VolunteerRecord.create(UUID.randomUUID(), "봉사2", LocalDate.of(2025, 1, 11), 100));
        volunteerRecordrepository.save(VolunteerRecord.create(UUID.randomUUID(), "봉사3", LocalDate.of(2025, 1, 12), 90));
        volunteerRecordrepository.save(VolunteerRecord.create(UUID.randomUUID(), "봉사4", LocalDate.of(2025, 1, 13), 60));
        volunteerRecordrepository.save(VolunteerRecord.create(UUID.randomUUID(), "봉사5", LocalDate.of(2025, 1, 14), 50));
        volunteerRecordrepository.save(VolunteerRecord.create(UUID.randomUUID(), "봉사6", LocalDate.of(2025, 1, 15), 30));
        
        // when
        List<Object[]> results = volunteerRecordrepository.findTotalTopRankingWithTies();

        // then
        // 총원 
        assertEquals(5, results.size());
        
        // 각 등수별 인원 확인
        assertEquals(100L, ((BigDecimal) results.get(0)[1]).longValue());
        assertEquals(100L, ((BigDecimal) results.get(1)[1]).longValue());
        assertEquals(90L, ((BigDecimal) results.get(2)[1]).longValue());
        assertEquals(60L, ((BigDecimal) results.get(3)[1]).longValue());
    }

    @DisplayName("주간 봉사 시간으로 4위까지의 랭킹을 반환할 수 있다. 동점자를 같이 반환한다. (Repository)")
    @Test
    void findVolunteerWeeklyRanking() {

        // given
        LocalDate currentDate = LocalDate.now();

        // 이번 주 데이터
        volunteerRecordrepository.save(VolunteerRecord.create(UUID.randomUUID(), "이번주 봉사1", currentDate, 100));
        volunteerRecordrepository.save(VolunteerRecord.create(UUID.randomUUID(), "이번주 봉사2", currentDate, 100));
        volunteerRecordrepository.save(VolunteerRecord.create(UUID.randomUUID(), "이번주 봉사3", currentDate, 90));
        volunteerRecordrepository.save(VolunteerRecord.create(UUID.randomUUID(), "이번주 봉사4", currentDate, 60));
        volunteerRecordrepository.save(VolunteerRecord.create(UUID.randomUUID(), "이번주 봉사5", currentDate, 50));
        volunteerRecordrepository.save(VolunteerRecord.create(UUID.randomUUID(), "이번주 봉사6", currentDate, 30));

        // 지난 주 데이터
        volunteerRecordrepository.save(VolunteerRecord.create(UUID.randomUUID(), "지난주봉사", currentDate.minusWeeks(1), 200));

        // when
        List<Object[]> results = volunteerRecordrepository.findWeeklyTopRankingWithTies();

        // then
        // 총원
        assertEquals(5, results.size());

        // 각 등수별 인원 확인
        assertEquals(100L, ((BigDecimal) results.get(0)[1]).longValue());
        assertEquals(100L, ((BigDecimal) results.get(1)[1]).longValue());
        assertEquals(90L, ((BigDecimal) results.get(2)[1]).longValue());
        assertEquals(60L, ((BigDecimal) results.get(3)[1]).longValue());
        assertEquals(50L, ((BigDecimal) results.get(4)[1]).longValue());

    }

    @DisplayName("월간 봉사 시간으로 4위까지의 랭킹을 반환할 수 있다. 동점자를 같이 반환한다. (Repository)")
    @Test
    void fineVolunteerMonthlyRanking() {

        // given
        LocalDate currentDate = LocalDate.now();

        // 이번 달 데이터
        volunteerRecordrepository.save(VolunteerRecord.create(UUID.randomUUID(), "이번달 봉사1", currentDate, 100));
        volunteerRecordrepository.save(VolunteerRecord.create(UUID.randomUUID(), "이번달 봉사2", currentDate, 100));
        volunteerRecordrepository.save(VolunteerRecord.create(UUID.randomUUID(), "이번달 봉사3", currentDate, 90));
        volunteerRecordrepository.save(VolunteerRecord.create(UUID.randomUUID(), "이번달 봉사4", currentDate, 60));
        volunteerRecordrepository.save(VolunteerRecord.create(UUID.randomUUID(), "이번달 봉사5", currentDate, 50));
        volunteerRecordrepository.save(VolunteerRecord.create(UUID.randomUUID(), "이번달 봉사6", currentDate, 30));

        // 지난 달 데이터
        volunteerRecordrepository.save(VolunteerRecord.create(UUID.randomUUID(), "지난달봉사", currentDate.minusMonths(1), 200));

        // when
        List<Object[]> results = volunteerRecordrepository.findMonthlyTopRankingWithTies();

        // then
        // 총원
        assertEquals(5, results.size());

        // 각 등수별 인원 확인
        assertEquals(100L, ((BigDecimal) results.get(0)[1]).longValue());
        assertEquals(100L, ((BigDecimal) results.get(1)[1]).longValue());
        assertEquals(90L, ((BigDecimal) results.get(2)[1]).longValue());
        assertEquals(60L, ((BigDecimal) results.get(3)[1]).longValue());
        assertEquals(50L, ((BigDecimal) results.get(4)[1]).longValue());
    }
}
