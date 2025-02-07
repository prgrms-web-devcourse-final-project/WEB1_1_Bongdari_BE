package com.somemore.user.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ImgUrlRequestDto(
        @Schema(description = "이미지 파일명")
        @NotBlank(message = "이미지 파일명은 필수 값입니다.")
        String fileName
) {
}
