package com.somemore.global.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "유저 정보 DTO")
public record UserInfoResponseDto(
        @JsonProperty("USER_ID")
        @Schema(description = "유저 ID")
        String userId,

        @JsonProperty("ROLE")
        @Schema(description = "유저 ROLE")
        String role
) {
}
