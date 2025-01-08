package com.somemore.global.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.somemore.user.domain.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "유저 정보 DTO")
public record UserInfoResponseDto(
        @JsonProperty("USER_ID")
        @Schema(description = "유저 ID")
        String userId,

        @JsonProperty("ROLE")
        @Schema(description = "유저 ROLE")
        String role
) {
        public static UserInfoResponseDto of(UUID userId, UserRole role) {
                return new UserInfoResponseDto(userId.toString(), role.getAuthority());
        }
}
