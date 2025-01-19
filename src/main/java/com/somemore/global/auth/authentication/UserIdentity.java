package com.somemore.global.auth.authentication;

import com.somemore.user.domain.UserRole;
import io.jsonwebtoken.Claims;

import java.util.UUID;

public record UserIdentity(
        UUID userId,
        UUID roleId,
        UserRole role
) {

    public static UserIdentity of(UUID userId, UUID roleId, UserRole role) {
        return new UserIdentity(userId, roleId, role);
    }

    public static UserIdentity from(Claims claims) {
        UUID userId = UUID.fromString(claims.get("userId", String.class));
        UUID roleId = UUID.fromString(claims.get("roleId", String.class));
        UserRole role = UserRole.from(
                claims.get("role", String.class));

        return new UserIdentity(userId, roleId, role);
    }
}
