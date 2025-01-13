package com.somemore.global.auth.oauth.processor;

import com.somemore.global.auth.oauth.domain.CustomOAuth2User;

import java.util.UUID;

public interface OAuthUserProcessor {
    UUID fetchUserIdByOAuthUser(CustomOAuth2User oauthUser);
}
