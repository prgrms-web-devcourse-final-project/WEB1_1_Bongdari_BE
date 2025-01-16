package com.somemore.global.auth.oauth.repository;

import com.somemore.global.auth.oauth.domain.OAuthInfo;
import com.somemore.global.auth.oauth.domain.OAuthProvider;

import java.util.Optional;
import java.util.UUID;

public interface OAuthInfoRepository {
    Optional<UUID> findUserIdByOAuthProviderAndOauthId(OAuthProvider provider, String oauthId);

    boolean existsByOAuthProviderAndOauthId(OAuthProvider provider, String oauthId);

    OAuthInfo save(OAuthInfo oauthInfo);
}
