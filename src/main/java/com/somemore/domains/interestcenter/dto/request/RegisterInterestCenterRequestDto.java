package com.somemore.domains.interestcenter.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.domains.interestcenter.domain.InterestCenter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RegisterInterestCenterRequestDto(

        @Schema(description = "기관 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotNull(message = "기관 ID는 필수값입니다.")
        UUID centerId
) {
    public InterestCenter toEntity(UUID volunteerId) {
        return InterestCenter.create(volunteerId, centerId);
    }
}
