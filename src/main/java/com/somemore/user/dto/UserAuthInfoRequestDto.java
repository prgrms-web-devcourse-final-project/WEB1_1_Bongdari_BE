package com.somemore.user.dto;

import com.somemore.global.auth.oauth.domain.OAuthProvider;

import java.util.UUID;

public record UserAuthInfoRequestDto(String email,
                                     String password) {

    public static UserAuthInfoRequestDto createForOAuth(OAuthProvider provider) {
        String email = provider.getProviderName() + UUID.randomUUID();
        String password = String.valueOf(UUID.randomUUID());
        return new UserAuthInfoRequestDto(email, password);
    }
}
