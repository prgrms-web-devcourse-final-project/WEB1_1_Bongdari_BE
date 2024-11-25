package com.somemore.center.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.center.domain.Center;
import com.somemore.community.dto.response.WriterDetailDto;
import com.somemore.volunteer.domain.Tier;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CenterForCommunityResponseDto(
        UUID id,
        String name,
        String imgUrl,
        Tier tier
) implements WriterDetailDto {

    public static CenterForCommunityResponseDto fromEntity(Center center) {
        return new CenterForCommunityResponseDto(
                center.getId(),
                center.getName(),
                center.getImgUrl(),
                null
        );
    }
}

