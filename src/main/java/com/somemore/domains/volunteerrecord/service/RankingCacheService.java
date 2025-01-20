package com.somemore.domains.volunteerrecord.service;

import com.somemore.domains.volunteerrecord.dto.response.VolunteerRankingResponseDto;
import com.somemore.domains.volunteerrecord.repository.VolunteerRankingRedisRepository;
import com.somemore.domains.volunteerrecord.usecase.RankingCacheUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RankingCacheService implements RankingCacheUseCase {

    private final VolunteerRankingRedisRepository volunteerRankingRedisRepository;

    @Override
    public void cacheRanking(VolunteerRankingResponseDto rankings) {
        volunteerRankingRedisRepository.saveRanking(rankings);
    }
}
