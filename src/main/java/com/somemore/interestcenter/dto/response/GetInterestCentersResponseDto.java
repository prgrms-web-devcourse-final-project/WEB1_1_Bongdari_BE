package com.somemore.interestcenter.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.center.dto.response.CenterOverviewInfoResponseDto;
import lombok.Builder;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record GetInterestCentersResponseDto(
        UUID centerId,
        String centerName,
        String imgUrl
) {
    public static GetInterestCentersResponseDto of(CenterOverviewInfoResponseDto responseDto) {
        return GetInterestCentersResponseDto.builder()
                .centerId(responseDto.centerId())
                .centerName(responseDto.centerName())
                .imgUrl(responseDto.imgUrl())
                .build();
    }
}
