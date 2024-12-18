package com.somemore.domains.center.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record CenterProfileUpdateRequestDto(
        @Schema(description = "센터 이름", example = "서울 도서관")
        @NotBlank(message = "센터 이름은 필수 값입니다.")
        String name,
        @Schema(description = "연락처", example = "010-1234-5678")
        @NotBlank(message = "연락처는 필수 값입니다.")
        String contactNumber,
        @Schema(description = "센터 홈페이지 링크", example = "https://fitnesscenter.com")
        @NotBlank(message = "센터 홈페이지 링크는 필수 값입니다.")
        String homepageLink,
        @Schema(description = "센터 소개", example = "저희 도서관은 유명해요")
        @NotBlank(message = "센터 소개는 필수 값입니다.")
        String introduce
) {}
