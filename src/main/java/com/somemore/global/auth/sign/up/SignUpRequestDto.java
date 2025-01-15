package com.somemore.global.auth.sign.up;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record SignUpRequestDto(

        @Schema(description = "계정 ID", example = "making")
        @NotBlank(message = "계정 ID는 필수 값이며 비어 있을 수 없습니다.")
        String accountId,

        @Schema(description = "계정 비밀번호", example = "password")
        @NotBlank(message = "계정 비밀번호는 필수 값이며 비어 있을 수 없습니다.")
        String accountPassword,

        @Schema(description = "유저 역할", example = "VOLUNTEER", allowableValues = {"VOLUNTEER", "CENTER"})
        @NotBlank(message = "유저 역할은 필수 값이며 비어 있을 수 없습니다.")
        String userRole
) {
}