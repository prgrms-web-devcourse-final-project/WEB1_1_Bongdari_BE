package com.somemore.user.dto.basicinfo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonNaming(SnakeCaseStrategy.class)
public record VolunteerBasicInfoRequestDto(
        @Schema(description = "공통 기본 정보")
        @Valid @NotNull(message = "공통 기본 정보는 필수 값입니다.")
        CommonBasicInfoRequestDto commonBasicInfo,

        @Schema(description = "닉네임", example = "칠가이")
        @NotBlank(message = "닉네임은 필수 값입니다.")
        String nickname,

        @Schema(description = "성별", example = "MALE", allowableValues = {"MALE", "FEMALE"})
        @NotBlank(message = "성별은 필수 값입니다.")
        String gender
) {

}
