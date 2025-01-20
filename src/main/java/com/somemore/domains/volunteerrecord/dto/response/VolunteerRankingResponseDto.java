package com.somemore.domains.volunteerrecord.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.domains.center.dto.response.PreferItemResponseDto;
import lombok.Builder;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record VolunteerRankingResponseDto(
        List<VolunteerTotalRankingResponseDto> volunteerTotalRankingResponse,
        List<VolunteerMonthlyRankingResponseDto> volunteerMonthlyRankingResponse,
        List<VolunteerWeeklyRankingResponseDto> volunteerWeeklyRankingResponse
) {
    public static VolunteerRankingResponseDto of(
            List<VolunteerTotalRankingResponseDto> totalRanking,
            List<VolunteerMonthlyRankingResponseDto> monthlyRanking,
            List<VolunteerWeeklyRankingResponseDto> weeklyRanking){

        return VolunteerRankingResponseDto.builder()
                .volunteerTotalRankingResponse(totalRanking)
                .volunteerMonthlyRankingResponse(monthlyRanking)
                .volunteerWeeklyRankingResponse(weeklyRanking)
                .build();
    }
}
