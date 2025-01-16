package com.somemore.domains.volunteerrecord.service;

import com.somemore.domains.volunteerrecord.dto.response.VolunteerRankingResponseDto;
import com.somemore.domains.volunteerrecord.repository.VolunteerRankingRedisRepository;
import com.somemore.domains.volunteerrecord.usecase.GetVolunteerRankingUseCase;
import com.somemore.global.exception.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_VOLUNTEER;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GetVolunteerRankingService implements GetVolunteerRankingUseCase {

    private final VolunteerRankingRedisRepository volunteerRankingRedisRepository;

    @Override
    public VolunteerRankingResponseDto getVolunteerRanking() {
        return volunteerRankingRedisRepository.getRankings()
                .orElseThrow(() -> new NoSuchElementException(NOT_EXISTS_VOLUNTEER));
    }

}
