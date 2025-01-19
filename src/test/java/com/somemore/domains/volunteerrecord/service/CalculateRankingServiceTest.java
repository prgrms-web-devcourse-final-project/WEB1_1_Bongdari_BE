package com.somemore.domains.volunteerrecord.service;

import com.somemore.domains.volunteerrecord.domain.VolunteerRecord;
import com.somemore.domains.volunteerrecord.dto.response.VolunteerRankingResponseDto;
import com.somemore.domains.volunteerrecord.repository.VolunteerRecordRepository;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class CalculateRankingServiceTest extends IntegrationTestSupport {

    @Autowired
    private CalculateRankingService calculateRankingService;

    @Autowired
    private VolunteerRecordRepository volunteerRecordRepository;

    @DisplayName("전체 기간 봉사 시간 합계로 4위까지의 랭킹을 반환할 수 있다. 동점자를 같이 반환한다.")
    @Test
    void calculateTotalVolunteerRanking() {

        // given
        volunteerRecordRepository.save(VolunteerRecord.create(UUID.randomUUID(), "봉사1", LocalDate.of(2025, 1, 10), 100));
        volunteerRecordRepository.save(VolunteerRecord.create(UUID.randomUUID(), "봉사2", LocalDate.of(2025, 1, 11), 100));
        volunteerRecordRepository.save(VolunteerRecord.create(UUID.randomUUID(), "봉사3", LocalDate.of(2025, 1, 12), 90));
        volunteerRecordRepository.save(VolunteerRecord.create(UUID.randomUUID(), "봉사4", LocalDate.of(2025, 1, 13), 60));
        volunteerRecordRepository.save(VolunteerRecord.create(UUID.randomUUID(), "봉사5", LocalDate.of(2025, 1, 14), 50));
        volunteerRecordRepository.save(VolunteerRecord.create(UUID.randomUUID(), "봉사6", LocalDate.of(2025, 1, 15), 30));

        // when
        VolunteerRankingResponseDto ranking = calculateRankingService.calculateRanking();

        // then
        assertThat(ranking.volunteerTotalRankingResponse()).hasSize(5);

        assertThat(ranking.volunteerTotalRankingResponse().get(0).totalHours()).isEqualTo(100L);
        assertThat(ranking.volunteerTotalRankingResponse().get(0).ranking()).isEqualTo(1);

        assertThat(ranking.volunteerTotalRankingResponse().get(1).totalHours()).isEqualTo(100L);
        assertThat(ranking.volunteerTotalRankingResponse().get(1).ranking()).isEqualTo(1);

        assertThat(ranking.volunteerTotalRankingResponse().get(3).totalHours()).isEqualTo(60L);
        assertThat(ranking.volunteerTotalRankingResponse().get(3).ranking()).isEqualTo(3);
    }

    @DisplayName("주간 봉사 시간으로 4위까지의 랭킹을 반환할 수 있다. 동점자를 같이 반환한다.")
    @Test
    void calculateWeeklyVolunteerRanking() {

        // given
        LocalDate currentDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

        // 이번 주 데이터
        volunteerRecordRepository.save(VolunteerRecord.create(UUID.randomUUID(), "이번주봉사1", currentDate, 100));
        volunteerRecordRepository.save(VolunteerRecord.create(UUID.randomUUID(), "이번주봉사2", currentDate, 100));
        volunteerRecordRepository.save(VolunteerRecord.create(UUID.randomUUID(), "이번주봉사3", currentDate, 90));
        volunteerRecordRepository.save(VolunteerRecord.create(UUID.randomUUID(), "이번주봉사4", currentDate, 60));
        volunteerRecordRepository.save(VolunteerRecord.create(UUID.randomUUID(), "이번주봉사5", currentDate, 50));

        // 지난 주 데이터
        volunteerRecordRepository.save(VolunteerRecord.create(UUID.randomUUID(), "지난주봉사", currentDate.minusWeeks(1), 200));

        // when
        VolunteerRankingResponseDto ranking = calculateRankingService.calculateRanking();

        // then
        assertThat(ranking.volunteerWeeklyRankingResponse()).hasSize(5);

        assertThat(ranking.volunteerWeeklyRankingResponse().get(0).totalHours()).isEqualTo(100L);
        assertThat(ranking.volunteerWeeklyRankingResponse().get(0).ranking()).isEqualTo(1);

        assertThat(ranking.volunteerWeeklyRankingResponse().get(1).totalHours()).isEqualTo(100L);
        assertThat(ranking.volunteerWeeklyRankingResponse().get(1).ranking()).isEqualTo(1);

        assertThat(ranking.volunteerWeeklyRankingResponse().get(3).totalHours()).isEqualTo(60L);
        assertThat(ranking.volunteerWeeklyRankingResponse().get(3).ranking()).isEqualTo(3);
    }

    @DisplayName("월간 봉사 시간으로 4위까지의 랭킹을 반환할 수 있다. 동점자를 같이 반환한다.")
    @Test
    void calculateMonthlyVolunteerRanking() {

        // given
        LocalDate currentDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

        // 이번 달 데이터
        volunteerRecordRepository.save(VolunteerRecord.create(UUID.randomUUID(), "이번달봉사1", currentDate, 100));
        volunteerRecordRepository.save(VolunteerRecord.create(UUID.randomUUID(), "이번달봉사2", currentDate, 100));
        volunteerRecordRepository.save(VolunteerRecord.create(UUID.randomUUID(), "이번달봉사3", currentDate, 90));
        volunteerRecordRepository.save(VolunteerRecord.create(UUID.randomUUID(), "이번달봉사4", currentDate, 60));
        volunteerRecordRepository.save(VolunteerRecord.create(UUID.randomUUID(), "이번달봉사5", currentDate, 50));

        // 지난 달 데이터
        volunteerRecordRepository.save(VolunteerRecord.create(UUID.randomUUID(), "지난달봉사", currentDate.minusMonths(1), 200));

        // when
        VolunteerRankingResponseDto ranking = calculateRankingService.calculateRanking();

        // then
        assertThat(ranking.volunteerMonthlyResponse()).hasSize(5);

        assertThat(ranking.volunteerMonthlyResponse().get(0).totalHours()).isEqualTo(100L);
        assertThat(ranking.volunteerMonthlyResponse().get(0).ranking()).isEqualTo(1);

        assertThat(ranking.volunteerMonthlyResponse().get(1).totalHours()).isEqualTo(100L);
        assertThat(ranking.volunteerMonthlyResponse().get(1).ranking()).isEqualTo(1);

        assertThat(ranking.volunteerMonthlyResponse().get(3).totalHours()).isEqualTo(60L);
        assertThat(ranking.volunteerMonthlyResponse().get(3).ranking()).isEqualTo(3);
    }
}
