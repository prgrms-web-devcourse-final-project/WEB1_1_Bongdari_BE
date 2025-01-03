package com.somemore.global.auth.oauth.domain;

public record CommonOAuthInfo(
        OAuthProvider provider,
        String oauthId) {
}
