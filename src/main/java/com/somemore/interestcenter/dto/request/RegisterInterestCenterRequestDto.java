package com.somemore.interestcenter.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.interestcenter.domain.InterestCenter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RegisterInterestCenterRequestDto(
        @Schema(description = "봉사자 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID volunteerId,

        @Schema(description = "봉사자 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID centerId
) {
    public InterestCenter toEntity(){
        return InterestCenter.create(volunteerId, centerId);
    }
}
