package com.somemore.domains.volunteerrecord.service;

import com.somemore.domains.volunteerrecord.dto.response.VolunteerMonthlyRankingResponseDto;
import com.somemore.domains.volunteerrecord.dto.response.VolunteerRankingResponseDto;
import com.somemore.domains.volunteerrecord.dto.response.VolunteerTotalRankingResponseDto;
import com.somemore.domains.volunteerrecord.dto.response.VolunteerWeeklyRankingResponseDto;
import com.somemore.domains.volunteerrecord.repository.VolunteerRecordRepository;
import com.somemore.domains.volunteerrecord.repository.mapper.VolunteerRankingMapper;
import com.somemore.domains.volunteerrecord.usecase.CalculateRankingUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CalculateRankingService implements CalculateRankingUseCase {

    private final VolunteerRecordRepository volunteerRecordRepository;

    @Override
    public VolunteerRankingResponseDto calculateRanking() {

        List<Object[]> volunteerTotalRanking = volunteerRecordRepository.findTotalTopRankingWithTies();
        List<Object[]> volunteerMonthlyRanking = volunteerRecordRepository.findMonthlyTopRankingWithTies();
        List<Object[]> volunteerWeeklyRanking = volunteerRecordRepository.findWeeklyTopRankingWithTies();

        List<VolunteerTotalRankingResponseDto> totalRankingDtos = volunteerTotalRanking.stream()
                .map(VolunteerRankingMapper::toTotalRankingResponse)
                .toList();

        List<VolunteerMonthlyRankingResponseDto> monthlyRankingDtos = volunteerMonthlyRanking.stream()
                .map(VolunteerRankingMapper::toMonthlyRankingResponse)
                .toList();

        List<VolunteerWeeklyRankingResponseDto> weeklyRankingDtos = volunteerWeeklyRanking.stream()
                .map(VolunteerRankingMapper::toWeeklyRankingResponse)
                .toList();

        return VolunteerRankingResponseDto.of(
                totalRankingDtos,
                monthlyRankingDtos,
                weeklyRankingDtos
        );
    }
}
