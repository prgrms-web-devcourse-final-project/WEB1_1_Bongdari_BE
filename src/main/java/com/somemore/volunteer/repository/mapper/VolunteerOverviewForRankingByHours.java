package com.somemore.volunteer.repository.mapper;

import com.somemore.volunteer.domain.Tier;

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
