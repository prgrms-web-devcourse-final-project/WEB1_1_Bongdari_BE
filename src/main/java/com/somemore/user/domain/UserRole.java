package com.somemore.user.domain;

import com.somemore.global.auth.jwt.exception.JwtErrorType;
import com.somemore.global.auth.jwt.exception.JwtException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum UserRole implements GrantedAuthority {
    VOLUNTEER("ROLE_VOLUNTEER", "봉사자"),
    CENTER("ROLE_CENTER", "기관"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String authority;

    @Getter
    private final String description;

    @Override
    public String getAuthority() {
        return this.authority;
    }

    public static UserRole getOAuthUserDefaultRole() {
        return VOLUNTEER;
    }

    public static UserRole from(String role) {
        for (UserRole userRole : values()) {
            if (role.contains(userRole.name())) {
                return userRole;
            }
        }
        throw new JwtException(JwtErrorType.UNKNOWN_ERROR);
    }


}
