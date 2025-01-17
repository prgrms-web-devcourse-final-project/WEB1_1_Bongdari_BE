package com.somemore.domains.volunteerrecord.scheduler;

import com.somemore.domains.volunteerrecord.dto.response.VolunteerRankingResponseDto;
import com.somemore.domains.volunteerrecord.usecase.CalculateRankingUseCase;
import com.somemore.domains.volunteerrecord.usecase.RankingCacheUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class VolunteerRankingCalculateScheduler {

    private final CalculateRankingUseCase calculateRankingUseCase;
    private final RankingCacheUseCase rankingCacheUseCase;

    private static final String CALCULATE_RANK_TIME = "0 50 * * * *";

    @Scheduled(cron = CALCULATE_RANK_TIME)
    public void cacheVolunteerRanking() {
        log.info("봉사 시간 랭킹 집계 시작");

        VolunteerRankingResponseDto rankings = calculateRankingUseCase.calculateRanking();

        rankingCacheUseCase.cacheRanking(rankings);

        log.info("봉사 시간 랭킹 집계 종료");
    }
}
