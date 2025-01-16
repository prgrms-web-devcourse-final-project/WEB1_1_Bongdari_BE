package com.somemore.domains.volunteerrecord.scheduler;

import com.somemore.domains.volunteerrecord.dto.response.VolunteerRankingResponseDto;
import com.somemore.domains.volunteerrecord.usecase.CalculateRankingUseCase;
import com.somemore.domains.volunteerrecord.usecase.RankingCacheUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class VolunteerRankingCalculateScheduler {

    private final CalculateRankingUseCase calculateRankingUseCase;
    private final RankingCacheUseCase rankingCacheUseCase;

    private static final String CALCULATE_RANK_TIME = "0 50 * * * *";

    @Scheduled(cron = CALCULATE_RANK_TIME)
    public void cacheVolunteerRanking() {

        VolunteerRankingResponseDto rankings = calculateRankingUseCase.calculateRanking();

        rankingCacheUseCase.cacheRanking(rankings);
    }
}
