package com.somemore.domains.interestcenter.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.domains.interestcenter.domain.InterestCenter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record RegisterInterestCenterResponseDto(
        @Schema(description = "관심 ID", example = "1111")
        Long id,

        @Schema(description = "봉사자 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID volunteerId,

        @Schema(description = "기관 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID centerId
) {
        public static RegisterInterestCenterResponseDto from(InterestCenter interestCenter) {
                return RegisterInterestCenterResponseDto.builder()
                        .id(interestCenter.getId())
                        .volunteerId(interestCenter.getVolunteerId())
                        .centerId(interestCenter.getCenterId())
                        .build();
        }
}
