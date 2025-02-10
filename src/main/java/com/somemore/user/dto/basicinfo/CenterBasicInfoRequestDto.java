package com.somemore.user.dto.basicinfo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CenterBasicInfoRequestDto(
        @Schema(description = "공통 기본 정보")
        @Valid @NotNull(message = "공통 기본 정보는 필수 값입니다.")
        CommonBasicInfoRequestDto commonBasicInfo,

        @Schema(description = "홈페이지 링크")
        @NotBlank(message = "홈페이지 링크는 필수 값입니다.")
        String homepageUrl
) {

}
