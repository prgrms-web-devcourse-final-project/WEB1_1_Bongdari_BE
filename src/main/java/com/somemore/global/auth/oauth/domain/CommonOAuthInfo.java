package com.somemore.global.auth.oauth.domain;

public record CommonOAuthInfo(
        OAuthProvider provider,
        String oauthId) {

    public static CommonOAuthInfo of(OAuthProvider provider, String oauthId) {
        return new CommonOAuthInfo(provider, oauthId);
    }
}
