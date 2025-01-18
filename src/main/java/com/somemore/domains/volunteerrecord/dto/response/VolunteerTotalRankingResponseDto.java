package com.somemore.domains.volunteerrecord.dto.response;

import java.util.UUID;

public record VolunteerTotalRankingResponseDto(
        UUID volunteerId,
        int totalHours,
        long ranking,
        String nickname
) {
}
