package com.somemore.domains.volunteerrecord.dto.response;

import java.util.UUID;

public record VolunteerWeeklyRankingResponseDto(
        UUID volunteerId,
        int totalHours,
        long ranking
) {
}
