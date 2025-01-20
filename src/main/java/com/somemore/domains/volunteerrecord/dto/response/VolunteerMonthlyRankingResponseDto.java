package com.somemore.domains.volunteerrecord.dto.response;

import java.util.UUID;

public record VolunteerMonthlyRankingResponseDto(
        UUID volunteerId,
        int totalHours,
        long ranking,
        String nickname
) {
}
