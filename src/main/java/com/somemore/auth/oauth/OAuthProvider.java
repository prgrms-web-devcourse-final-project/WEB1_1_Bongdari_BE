package com.somemore.auth.oauth;

import lombok.Getter;

@Getter
public enum OAuthProvider {
    NAVER("naver");

    private final String providerName;

    OAuthProvider(String providerName) {
        this.providerName = providerName;
    }

    public static OAuthProvider from(String providerName) {
        for (OAuthProvider provider : values()) {
            if (provider.providerName.equals(providerName)) {
                return provider;
            }
        }

        throw new IllegalArgumentException("올바르지 않은 OAuth 제공자: " + providerName);
    }
}