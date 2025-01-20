package com.somemore.domains.volunteerrecord.usecase;

import com.somemore.domains.volunteerrecord.dto.response.VolunteerRankingResponseDto;

public interface RankingCacheUseCase {
    void cacheRanking(VolunteerRankingResponseDto rankings);
}
