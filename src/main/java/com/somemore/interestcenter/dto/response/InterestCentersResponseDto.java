package com.somemore.interestcenter.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.center.repository.mapper.CenterOverviewInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record InterestCentersResponseDto(
        @Schema(description = "관심기관의 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID centerId,
        @Schema(description = "관심기관의 이름", example = "서울 도서관")
        String centerName,
        @Schema(description = "관심기관의 프로필 이미지 링크", example = "~~/image.jpeg")
        String imgUrl
) {
    public static InterestCentersResponseDto of(CenterOverviewInfo responseDto) {
        return InterestCentersResponseDto.builder()
                .centerId(responseDto.centerId())
                .centerName(responseDto.centerName())
                .imgUrl(responseDto.imgUrl())
                .build();
    }
}
