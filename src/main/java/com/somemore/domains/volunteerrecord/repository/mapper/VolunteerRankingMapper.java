package com.somemore.domains.volunteerrecord.repository.mapper;

import com.somemore.domains.volunteerrecord.dto.response.VolunteerMonthlyRankingResponseDto;
import com.somemore.domains.volunteerrecord.dto.response.VolunteerTotalRankingResponseDto;
import com.somemore.domains.volunteerrecord.dto.response.VolunteerWeeklyRankingResponseDto;

import java.util.UUID;

public class VolunteerRankingMapper {

    private VolunteerRankingMapper() {
        throw new UnsupportedOperationException("유틸리티 클래스는 인스턴스화할 수 없습니다.");
    }

    public static VolunteerTotalRankingResponseDto toTotalRankingResponse(Object[] result) {
        return new VolunteerTotalRankingResponseDto(
                (UUID) result[0],
                ((Number) result[1]).intValue(),
                ((Number) result[2]).longValue()
        );
    }

    public static VolunteerWeeklyRankingResponseDto toWeeklyRankingResponse(Object[] result) {
        return new VolunteerWeeklyRankingResponseDto(
                (UUID) result[0],
                ((Number) result[1]).intValue(),
                ((Number) result[2]).longValue()
        );
    }

    public static VolunteerMonthlyRankingResponseDto toMonthlyRankingResponse(Object[] result) {
        return new VolunteerMonthlyRankingResponseDto(
                (UUID) result[0],
                ((Number) result[1]).intValue(),
                ((Number) result[2]).longValue()
        );
    }
}
