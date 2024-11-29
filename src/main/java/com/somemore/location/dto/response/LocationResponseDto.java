package com.somemore.location.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.location.domain.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
@JsonNaming(SnakeCaseStrategy.class)
@Schema(description = "위치 조회 응답 DTO")
public record LocationResponseDto(
    @Schema(description = "주소", example = "서울특별시 강남구 테헤란로 123")
    String address,
    @Schema(description = "위도", example = "37.5665")
    BigDecimal latitude,
    @Schema(description = "경도", example = "126.9780")
    BigDecimal longitude
) {

    public static LocationResponseDto from(Location location) {
        return LocationResponseDto.builder()
            .address(location.getAddress())
            .latitude(location.getLatitude())
            .longitude(location.getLongitude())
            .build();
    }

    public static LocationResponseDto of(String address, BigDecimal latitude,
        BigDecimal longitude) {
        return LocationResponseDto.builder()
            .address(address)
            .latitude(latitude)
            .longitude(longitude)
            .build();
    }
}
