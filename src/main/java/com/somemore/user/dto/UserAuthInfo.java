package com.somemore.user.dto;

import com.somemore.global.auth.oauth.domain.OAuthProvider;
import com.somemore.global.encoder.BCryptPasswordEncoderUtil;

import java.util.UUID;

public record UserAuthInfo(String accountId,
                           String accountPassword) {
    public UserAuthInfo(String accountId,
                        String accountPassword) {
        this.accountId = accountId;
        this.accountPassword = BCryptPasswordEncoderUtil.encode(accountPassword);
    }

    public static UserAuthInfo of(String accountId, String accountPassword) {
        return new UserAuthInfo(accountId, accountPassword);
    }

    public static UserAuthInfo createForOAuth(OAuthProvider provider) {
        String accountId = provider.getProviderName() + UUID.randomUUID();
        String accountPassword = String.valueOf(UUID.randomUUID());
        return new UserAuthInfo(accountId, accountPassword);
    }
}
