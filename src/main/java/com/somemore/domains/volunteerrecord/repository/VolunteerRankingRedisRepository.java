package com.somemore.domains.volunteerrecord.repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class VolunteerRankingRedisRepository implements VolunteerRankingCacheRepository{

    private final RedisTemplate<String, Object> redisTemplate;

    private static final Duration CACHE_TTL = Duration.ofMinutes(60);
    private static final String TOTAL_RANKING_KEY = "volunteer:total_ranking";
    private static final String MONTHLY_RANKING_KEY = "volunteer:monthly_ranking";
    private static final String WEEKLY_RANKING_KEY = "volunteer:weekly_ranking";

    public void saveRanking(VolunteerRankingResponseDto rankings) {
        redisTemplate.opsForValue().set(TOTAL_RANKING_KEY, rankings.volunteerTotalRankingResponse(), CACHE_TTL);
        redisTemplate.opsForValue().set(MONTHLY_RANKING_KEY, rankings.volunteerMonthlyRankingResponse(), CACHE_TTL);
        redisTemplate.opsForValue().set(WEEKLY_RANKING_KEY, rankings.volunteerWeeklyRankingResponse(), CACHE_TTL);
    }

    @SuppressWarnings("unchecked")
    public Optional<VolunteerRankingResponseDto> getRankings() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        List<VolunteerTotalRankingResponseDto> totalRanking =
                (List<VolunteerTotalRankingResponseDto>) Optional.ofNullable(redisTemplate.opsForValue().get(TOTAL_RANKING_KEY))
                        .map(obj -> mapper.convertValue(obj,
                                mapper.getTypeFactory().constructCollectionType(List.class, VolunteerTotalRankingResponseDto.class)))
                        .orElse(null);

        List<VolunteerMonthlyRankingResponseDto> monthlyRanking =
                (List<VolunteerMonthlyRankingResponseDto>) Optional.ofNullable(redisTemplate.opsForValue().get(MONTHLY_RANKING_KEY))
                        .map(obj -> mapper.convertValue(obj,
                                mapper.getTypeFactory().constructCollectionType(List.class, VolunteerMonthlyRankingResponseDto.class)))
                        .orElse(null);

        List<VolunteerWeeklyRankingResponseDto> weeklyRanking =
                (List<VolunteerWeeklyRankingResponseDto>) Optional.ofNullable(redisTemplate.opsForValue().get(WEEKLY_RANKING_KEY))
                        .map(obj -> mapper.convertValue(obj,
                                mapper.getTypeFactory().constructCollectionType(List.class, VolunteerWeeklyRankingResponseDto.class)))
                        .orElse(null);

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
