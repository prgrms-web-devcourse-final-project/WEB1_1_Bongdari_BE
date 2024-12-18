package com.somemore.domains.volunteer.repository.mapper;

import com.somemore.domains.volunteer.domain.Tier;

import java.util.UUID;

public record VolunteerSimpleInfo(
        UUID id,
        String name,
        String nickname,
        String email,
        String imgUrl,
        Tier tier
) {

}
