package com.somemore.user.dto;

import com.somemore.global.auth.oauth.domain.OAuthProvider;

import java.util.UUID;

public record UserAuthInfo(String accountId,
                           String password) {

    public static UserAuthInfo createForOAuth(OAuthProvider provider) {
        String accountId = provider.getProviderName() + UUID.randomUUID();
        String password = String.valueOf(UUID.randomUUID());
        return new UserAuthInfo(accountId, password);
    }
}
