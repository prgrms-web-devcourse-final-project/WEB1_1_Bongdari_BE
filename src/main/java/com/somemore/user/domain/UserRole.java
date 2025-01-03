package com.somemore.user.domain;

import com.somemore.global.auth.jwt.exception.JwtErrorType;
import com.somemore.global.auth.jwt.exception.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum UserRole implements GrantedAuthority {
    VOLUNTEER("ROLE_VOLUNTEER"),
    CENTER("ROLE_CENTER"),
    ADMIN("ROLE_ADMIN");

    private final String authority;

    @Override
    public String getAuthority() {
        return this.authority;
    }

    public static UserRole getOAuthUserDefaultRole() {
        return VOLUNTEER;
    }

    public static UserRole from(String role) {
        for (UserRole userRole : values()) {
            if (userRole.name().equals(role)) {
                return userRole;
            }
        }
        throw new JwtException(JwtErrorType.UNKNOWN_ERROR);
    }
}
