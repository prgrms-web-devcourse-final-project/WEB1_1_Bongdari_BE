package com.somemore.user.dto.basicinfo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CommonBasicInfoRequestDto(
        @Schema(description = "이름", example = "홍길동, 롯데리아")
        @NotBlank(message = "이름은 필수 값입니다.")
        String name,

        @Schema(description = "연락처", example = "010-1234-5678, 02-123-4567")
        @NotBlank(message = "연락처는 필수 값입니다.")
        String contactNumber,

        @Schema(description = "이미지 링크", example = "https://image.image")
        @NotBlank(message = "이미지 링크는 필수 값입니다.")
        String imgUrl,

        @Schema(description = "소개 글", example = "햄부기햄북햄북어햄북스딱스함부르크햄부가우가햄비기햄부거햄부가티햄부기온앤온을 차려오거라")
        @NotBlank(message = "소개 글은 필수 값입니다.")
        String introduce
) {
}
