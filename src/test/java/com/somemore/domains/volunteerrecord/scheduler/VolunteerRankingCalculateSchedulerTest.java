package com.somemore.domains.volunteerrecord.scheduler;

import com.somemore.domains.volunteerrecord.dto.response.VolunteerRankingResponseDto;
import com.somemore.domains.volunteerrecord.usecase.CalculateRankingUseCase;
import com.somemore.domains.volunteerrecord.usecase.RankingCacheUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VolunteerRankingCalculateSchedulerTest {

    @InjectMocks
    private VolunteerRankingCalculateScheduler volunteerRankingCalculateScheduler;

    @Mock
    private CalculateRankingUseCase calculateRankingUseCase;

    @Mock
    private RankingCacheUseCase rankingCacheUseCase;

    @Mock
    private VolunteerRankingResponseDto volunteerRankingResponseDto;

    @DisplayName("봉사 랭킹 계산 및 캐시 저장을 정상적으로 호출할 수 있다")
    @Test
    void cacheVolunteerRanking_Success() {

        // given
        when(calculateRankingUseCase.calculateRanking()).thenReturn(volunteerRankingResponseDto);

        // when
        volunteerRankingCalculateScheduler.cacheVolunteerRanking();

        // then
        verify(calculateRankingUseCase, times(1)).calculateRanking();
        verify(rankingCacheUseCase, times(1)).cacheRanking(volunteerRankingResponseDto);
    }

}
