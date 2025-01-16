package com.somemore.domains.volunteerrecord.repository;

import com.somemore.domains.volunteerrecord.dto.response.VolunteerMonthlyRankingResponseDto;
import com.somemore.domains.volunteerrecord.dto.response.VolunteerRankingResponseDto;
import com.somemore.domains.volunteerrecord.dto.response.VolunteerTotalRankingResponseDto;
import com.somemore.domains.volunteerrecord.dto.response.VolunteerWeeklyRankingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class VolunteerRankingRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final Duration CACHE_TTL = Duration.ofMinutes(60);
    private static final String TOTAL_RANKING_KEY = "volunteer:total_ranking";
    private static final String MONTHLY_RANKING_KEY = "volunteer:monthly_ranking";
    private static final String WEEKLY_RANKING_KEY = "volunteer:weekly_ranking";

    public void saveRanking(VolunteerRankingResponseDto rankings) {
        redisTemplate.opsForValue().set(TOTAL_RANKING_KEY, rankings.volunteerTotalRankingResponse(), CACHE_TTL);
        redisTemplate.opsForValue().set(MONTHLY_RANKING_KEY, rankings.volunteerMonthlyResponse(), CACHE_TTL);
        redisTemplate.opsForValue().set(WEEKLY_RANKING_KEY, rankings.volunteerWeeklyRankingResponse(), CACHE_TTL);
    }

    @SuppressWarnings("unchecked")
    public Optional<VolunteerRankingResponseDto> getRankings() {

        List<VolunteerTotalRankingResponseDto> totalRanking =
                (List<VolunteerTotalRankingResponseDto>) redisTemplate.opsForValue().get(TOTAL_RANKING_KEY);
        List<VolunteerMonthlyRankingResponseDto> monthlyRanking =
                (List<VolunteerMonthlyRankingResponseDto>) redisTemplate.opsForValue().get(MONTHLY_RANKING_KEY);
        List<VolunteerWeeklyRankingResponseDto> weeklyRanking =
                (List<VolunteerWeeklyRankingResponseDto>) redisTemplate.opsForValue().get(WEEKLY_RANKING_KEY);

        if (totalRanking == null || monthlyRanking == null || weeklyRanking == null) {
            return Optional.empty();
        }

        return Optional.of(VolunteerRankingResponseDto.of(
                totalRanking,
                monthlyRanking,
                weeklyRanking
        ));
    }
}
