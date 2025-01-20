package com.somemore.domains.volunteerrecord.repository;

import com.somemore.domains.volunteerrecord.dto.response.VolunteerMonthlyRankingResponseDto;
import com.somemore.domains.volunteerrecord.dto.response.VolunteerRankingResponseDto;
import com.somemore.domains.volunteerrecord.dto.response.VolunteerTotalRankingResponseDto;
import com.somemore.domains.volunteerrecord.dto.response.VolunteerWeeklyRankingResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class VolunteerRankingRedisRepositoryTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private VolunteerRankingRedisRepository volunteerRankingRedisRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @DisplayName("봉사 랭킹 정보를 캐싱할 수 있다. (Repository)")
    @Test
    void saveRanking() {

        // given
        VolunteerRankingResponseDto rankings = createVolunteerRankingResponseDto();

        // when
        volunteerRankingRedisRepository.saveRanking(rankings);

        // then
        verify(valueOperations, times(1)).set("volunteer:total_ranking", rankings.volunteerTotalRankingResponse(), Duration.ofMinutes(60));
        verify(valueOperations, times(1)).set("volunteer:monthly_ranking", rankings.volunteerMonthlyRankingResponse(), Duration.ofMinutes(60));
        verify(valueOperations, times(1)).set("volunteer:weekly_ranking", rankings.volunteerWeeklyRankingResponse(), Duration.ofMinutes(60));
    }

    @DisplayName("캐싱된 봉사 랭킹 정보를 조회할 수 있다.")
    @Test
    void getRankings() {

        // given
        VolunteerRankingResponseDto rankings = createVolunteerRankingResponseDto();
        when(valueOperations.get("volunteer:total_ranking")).thenReturn(rankings.volunteerTotalRankingResponse());
        when(valueOperations.get("volunteer:monthly_ranking")).thenReturn(rankings.volunteerMonthlyRankingResponse());
        when(valueOperations.get("volunteer:weekly_ranking")).thenReturn(rankings.volunteerWeeklyRankingResponse());

        // when
        Optional<VolunteerRankingResponseDto> retrievedRankings = volunteerRankingRedisRepository.getRankings();

        // then
        assertTrue(retrievedRankings.isPresent());
        assertEquals(rankings, retrievedRankings.get());
    }

    @DisplayName("전체, 월, 주간 랭킹 하나라도 빈 값이 반환되면 빈 리스트를 반환한다.")
    @Test
    void getRankingsWhenDataNotFound() {

        // given
        when(valueOperations.get("volunteer:total_ranking")).thenReturn(null);
        when(valueOperations.get("volunteer:monthly_ranking")).thenReturn(null);
        when(valueOperations.get("volunteer:weekly_ranking")).thenReturn(null);

        // when
        Optional<VolunteerRankingResponseDto> retrievedRankings = volunteerRankingRedisRepository.getRankings();

        // then
        assertFalse(retrievedRankings.isPresent());
    }

    private VolunteerRankingResponseDto createVolunteerRankingResponseDto() {

        List<VolunteerTotalRankingResponseDto> totalRanking = new ArrayList<>();
        totalRanking.add(new VolunteerTotalRankingResponseDto(UUID.randomUUID(), 1, 100L, "봉사자1"));

        List<VolunteerMonthlyRankingResponseDto> monthlyRanking = new ArrayList<>();
        monthlyRanking.add(new VolunteerMonthlyRankingResponseDto(UUID.randomUUID(), 2, 200L, "봉사자1"));

        List<VolunteerWeeklyRankingResponseDto> weeklyRanking = new ArrayList<>();
        weeklyRanking.add(new VolunteerWeeklyRankingResponseDto(UUID.randomUUID(), 3, 300L, "봉사자1"));

        return new VolunteerRankingResponseDto(totalRanking, monthlyRanking, weeklyRanking);
    }
}
