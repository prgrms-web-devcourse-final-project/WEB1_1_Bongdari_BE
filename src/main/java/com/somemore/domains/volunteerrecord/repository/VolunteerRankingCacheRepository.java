package com.somemore.domains.volunteerrecord.repository;

import com.somemore.domains.volunteerrecord.dto.response.VolunteerRankingResponseDto;

import java.util.Optional;

public interface VolunteerRankingCacheRepository {
    void saveRanking(VolunteerRankingResponseDto rankings);
    Optional<VolunteerRankingResponseDto> getRankings();
}
