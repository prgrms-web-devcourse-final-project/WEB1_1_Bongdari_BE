package com.somemore.location.dto.request;


import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.location.domain.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@JsonNaming(SnakeCaseStrategy.class)
@Builder
public record LocationCreateRequestDto(
    @Schema(description = "도로명 주소", example = "서울특별시 서초구 반포대로 45, 4층(서초동, 명정빌딩)")
    @NotBlank(message = "주소는 필수 입력 값입니다.")
    String address,
    @Schema(description = "주소에 해당하는 위도 정보", example = "37.4845373748015")
    @NotBlank(message = "위도는 필수 입력 값입니다.")
    String latitude,
    @Schema(description = "주소에 해당하는 경도 정보", example = "127.010842267696")
    @NotBlank(message = "경도는 필수 입력 값입니다.")
    String longitude
) {

    public Location toEntity() {
        return Location.builder()
            .address(address)
            .latitude(latitude)
            .longitude(longitude)
            .build();
    }
}
