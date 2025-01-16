package com.somemore.domains.volunteerrecord.service;

import com.somemore.domains.volunteerrecord.dto.response.VolunteerMonthlyRankingResponseDto;
import com.somemore.domains.volunteerrecord.dto.response.VolunteerRankingResponseDto;
import com.somemore.domains.volunteerrecord.dto.response.VolunteerTotalRankingResponseDto;
import com.somemore.domains.volunteerrecord.dto.response.VolunteerWeeklyRankingResponseDto;
import com.somemore.domains.volunteerrecord.repository.VolunteerRankingRedisRepository;
import com.somemore.domains.volunteerrecord.repository.VolunteerRecordRepository;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@Transactional
class GetVolunteerRankingServiceTest extends IntegrationTestSupport {

    @Autowired
    private GetVolunteerRankingService getVolunteerRankingService;

    @Autowired
    private GetVolunteerRankingService volunteerRankingService;

    @Autowired
    private VolunteerRecordRepository volunteerRecordRepository;

    @Autowired
    private VolunteerRankingRedisRepository volunteerRankingRedisRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @DisplayName("캐싱된 봉사 랭킹 정보를 조회할 수 있다.")
    @Test
    void getVolunteerRanking_Success() {
        // given
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();
        UUID id4 = UUID.randomUUID();
        UUID id5 = UUID.randomUUID();
        UUID id6 = UUID.randomUUID();

        List<VolunteerTotalRankingResponseDto> totalRanking = List.of(
                new VolunteerTotalRankingResponseDto(id1, 100, 1),
                new VolunteerTotalRankingResponseDto(id2, 90, 2)
        );

        List<VolunteerMonthlyRankingResponseDto> monthlyRanking = List.of(
                new VolunteerMonthlyRankingResponseDto(id3, 50, 1),
                new VolunteerMonthlyRankingResponseDto(id4, 40, 2)
        );

        List<VolunteerWeeklyRankingResponseDto> weeklyRanking = List.of(
                new VolunteerWeeklyRankingResponseDto(id5, 30, 1),
                new VolunteerWeeklyRankingResponseDto(id6, 20, 2)
        );

        VolunteerRankingResponseDto rankings = VolunteerRankingResponseDto.of(
                totalRanking,
                monthlyRanking,
                weeklyRanking
        );

        volunteerRankingRedisRepository.saveRanking(rankings);

        // when
        VolunteerRankingResponseDto result = getVolunteerRankingService.getVolunteerRanking();

        // then
        assertThat(result.volunteerTotalRankingResponse())
                .hasSize(2)
                .extracting("volunteerId", "totalHours", "ranking")
                .containsExactly(
                        tuple(id1.toString(), 100, 1),
                        tuple(id2.toString(), 90, 2)
                );

        assertThat(result.volunteerMonthlyResponse())
                .hasSize(2)
                .extracting("volunteerId", "totalHours", "ranking")
                .containsExactly(
                        tuple(id3.toString(), 50, 1),
                        tuple(id4.toString(), 40, 2)
                );

        assertThat(result.volunteerWeeklyRankingResponse())
                .hasSize(2)
                .extracting("volunteerId", "totalHours", "ranking")
                .containsExactly(
                        tuple(id5.toString(), 30, 1),
                        tuple(id6.toString(), 20, 2)
                );

    }

}
