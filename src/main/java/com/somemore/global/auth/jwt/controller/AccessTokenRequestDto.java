package com.somemore.global.auth.jwt.controller;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AccessTokenRequestDto(

        @Schema(description = "액세스 토큰", example = "액세스 토큰 문자열")
        @NotBlank(message = "액세스 토큰 문자열")
        String accessToken
) {
}
