package com.somemore.location.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Builder;

@JsonNaming(SnakeCaseStrategy.class)
@Builder
public record LocationUpdateRequestDto(
    @Schema(description = "도로명 주소", example = "서울특별시 서초구 반포대로 45, 4층(서초동, 명정빌딩)")
    @NotBlank(message = "주소는 필수 입력 값입니다.")
    String address,
    @Schema(description = "주소에 해당하는 위도 정보", example = "37.4845373748015")
    @NotNull(message = "위도는 필수 입력 값입니다.")
    @DecimalMin(value = "33", message = "위도는 33도 이상이어야 합니다.")
    @DecimalMax(value = "39", message = "위도는 38도 이하이어야 합니다.")
    BigDecimal latitude,
    @Schema(description = "주소에 해당하는 경도 정보", example = "127.010842267696")
    @NotNull(message = "경도는 필수 입력 값입니다.")
    @DecimalMin(value = "124", message = "경도는 124도 이상이어야 합니다.")
    @DecimalMax(value = "132", message = "경도는 132도 이하이어야 합니다.")
    BigDecimal longitude
) {

}
