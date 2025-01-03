package com.somemore.global.auth.oauth.processor;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.UUID;

public interface OAuthUserProcessor {
    UUID fetchUserIdByOAuthUser(OAuth2User oauthUser);
}
