package com.somemore.domains.volunteer.repository.mapper;

import com.somemore.domains.volunteer.domain.Tier;

import java.util.UUID;

public record VolunteerOverviewForRankingByHours(
        UUID volunteerId,
        String nickname,
        String imgUrl,
        String introduce,
        Tier tier,
        Integer totalVolunteerHours
) {
}
