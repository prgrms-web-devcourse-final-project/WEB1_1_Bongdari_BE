package com.somemore.domains.volunteerrecord.service;

import com.somemore.domains.volunteerrecord.dto.response.VolunteerMonthlyRankingResponseDto;
import com.somemore.domains.volunteerrecord.dto.response.VolunteerRankingResponseDto;
import com.somemore.domains.volunteerrecord.dto.response.VolunteerTotalRankingResponseDto;
import com.somemore.domains.volunteerrecord.dto.response.VolunteerWeeklyRankingResponseDto;
import com.somemore.domains.volunteerrecord.repository.VolunteerRankingRedisRepository;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Transactional
class RankingCacheServiceTest extends IntegrationTestSupport {

    @Autowired
    private RankingCacheService rankingCacheService;

    @Autowired
    private VolunteerRankingRedisRepository volunteerRankingRedisRepository;

    @DisplayName("봉사 랭킹 데이터를 캐시에 저장할 수 있다.")
    @Test
    void cacheRanking_Success() {
        // given
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();
        UUID id4 = UUID.randomUUID();
        UUID id5 = UUID.randomUUID();
        UUID id6 = UUID.randomUUID();

        List<VolunteerTotalRankingResponseDto> totalRanking = List.of(
                new VolunteerTotalRankingResponseDto(id1, 100, 1, "봉사자1"),
                new VolunteerTotalRankingResponseDto(id2, 90, 2, "봉사자2")
        );

        List<VolunteerMonthlyRankingResponseDto> monthlyRanking = List.of(
                new VolunteerMonthlyRankingResponseDto(id3, 50, 1, "봉사자3"),
                new VolunteerMonthlyRankingResponseDto(id4, 40, 2, "봉사자4")
        );

        List<VolunteerWeeklyRankingResponseDto> weeklyRanking = List.of(
                new VolunteerWeeklyRankingResponseDto(id5, 30, 1, "봉사자5"),
                new VolunteerWeeklyRankingResponseDto(id6, 20, 2, "봉사자6")
        );

        VolunteerRankingResponseDto rankings = VolunteerRankingResponseDto.of(
                totalRanking,
                monthlyRanking,
                weeklyRanking
        );

        // when
        rankingCacheService.cacheRanking(rankings);

        // then
        Optional<VolunteerRankingResponseDto> cachedRankings = volunteerRankingRedisRepository.getRankings();

        assertThat(cachedRankings)
                .isPresent()
                .get()
                .satisfies(dto -> {
                    assertThat(dto.volunteerTotalRankingResponse())
                            .hasSize(2)
                            .extracting("volunteerId", "totalHours", "ranking")
                            .containsExactly(
                                    tuple(id1.toString(), 100, 1),
                                    tuple(id2.toString(), 90, 2)
                            );

                    assertThat(dto.volunteerMonthlyRankingResponse())
                            .hasSize(2)
                            .extracting("volunteerId", "totalHours", "ranking")
                            .containsExactly(
                                    tuple(id3.toString(), 50, 1),
                                    tuple(id4.toString(), 40, 2)
                            );

                    assertThat(dto.volunteerWeeklyRankingResponse())
                            .hasSize(2)
                            .extracting("volunteerId", "totalHours", "ranking")
                            .containsExactly(
                                    tuple(id5.toString(), 30, 1),
                                    tuple(id6.toString(), 20, 2)
                            );
                });
    }
}
