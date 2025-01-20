package com.somemore.domains.volunteerrecord.service;

import com.somemore.domains.volunteerrecord.dto.response.VolunteerMonthlyRankingResponseDto;
import com.somemore.domains.volunteerrecord.dto.response.VolunteerRankingResponseDto;
import com.somemore.domains.volunteerrecord.dto.response.VolunteerTotalRankingResponseDto;
import com.somemore.domains.volunteerrecord.dto.response.VolunteerWeeklyRankingResponseDto;
import com.somemore.domains.volunteerrecord.repository.VolunteerRecordRepository;
import com.somemore.domains.volunteerrecord.repository.mapper.VolunteerRankingMapper;
import com.somemore.domains.volunteerrecord.usecase.CalculateRankingUseCase;
import com.somemore.volunteer.repository.record.VolunteerNickname;
import com.somemore.volunteer.usecase.GetVolunteerNicknamesByIdsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CalculateRankingService implements CalculateRankingUseCase {

    private final GetVolunteerNicknamesByIdsUseCase getVolunteerNicknamesByIdsUseCase;

    private final VolunteerRecordRepository volunteerRecordRepository;

    @Override
    public VolunteerRankingResponseDto calculateRanking() {
        List<Object[]> volunteerTotalRanking = volunteerRecordRepository.findTotalTopRankingWithTies();
        List<Object[]> volunteerMonthlyRanking = volunteerRecordRepository.findMonthlyTopRankingWithTies();
        List<Object[]> volunteerWeeklyRanking = volunteerRecordRepository.findWeeklyTopRankingWithTies();

        List<List<Object[]>> rankings = List.of(volunteerTotalRanking, volunteerMonthlyRanking, volunteerWeeklyRanking);

        Map<UUID, String> volunteerIdToNickname = createVolunteerIdToNickname(rankings);

        return VolunteerRankingResponseDto.of(
                mapToTotalRankingDtos(volunteerTotalRanking, volunteerIdToNickname),
                mapToMonthlyRankingDtos(volunteerMonthlyRanking, volunteerIdToNickname),
                mapToWeeklyRankingDtos(volunteerWeeklyRanking, volunteerIdToNickname)
        );
    }

    private Map<UUID, String> createVolunteerIdToNickname(List<List<Object[]>> rankings) {
        Set<UUID> allVolunteerIds = rankings.stream()
                .flatMap(List::stream)
                .map(result -> VolunteerRankingMapper.toUUID(result[0]))
                .collect(Collectors.toSet());

        return getVolunteerNicknamesByIdsUseCase.getNicknamesByIds(new ArrayList<>(allVolunteerIds))
                .stream()
                .collect(Collectors.toMap(
                        VolunteerNickname::volunteerId,
                        VolunteerNickname::nickname
                ));
    }

    private List<VolunteerTotalRankingResponseDto> mapToTotalRankingDtos(List<Object[]> ranking, Map<UUID, String> volunteerIdToNickname) {
        return ranking.stream()
                .map(result -> VolunteerRankingMapper.toTotalRankingResponse(result, volunteerIdToNickname))
                .toList();
    }

    private List<VolunteerMonthlyRankingResponseDto> mapToMonthlyRankingDtos(List<Object[]> ranking, Map<UUID, String> volunteerIdToNickname) {
        return ranking.stream()
                .map(result -> VolunteerRankingMapper.toMonthlyRankingResponse(result, volunteerIdToNickname))
                .toList();
    }

    private List<VolunteerWeeklyRankingResponseDto> mapToWeeklyRankingDtos(List<Object[]> ranking, Map<UUID, String> volunteerIdToNickname) {
        return ranking.stream()
                .map(result -> VolunteerRankingMapper.toWeeklyRankingResponse(result, volunteerIdToNickname))
                .toList();
    }
}
