package com.somemore.volunteer.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.community.dto.response.WriterDetailDto;
import com.somemore.volunteer.domain.Tier;
import com.somemore.volunteer.domain.Volunteer;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record VolunteerForCommunityResponseDto(
        UUID id,
        String name,
        String imgUrl,
        Tier tier
) implements WriterDetailDto {

    public static VolunteerForCommunityResponseDto fromEntity(Volunteer volunteer) {
        return new VolunteerForCommunityResponseDto(
                volunteer.getId(),
                volunteer.getNickname(),
                volunteer.getImgUrl(),
                volunteer.getTier()
        );
    }
}

