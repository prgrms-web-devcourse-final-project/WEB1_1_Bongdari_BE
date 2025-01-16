package com.somemore.global.auth.jwt.manager;

import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.domain.RefreshToken;

import java.util.UUID;

public interface TokenManager {
    RefreshToken getRefreshTokenByAccessToken(EncodedToken accessToken);

    EncodedToken getAccessTokenByUserId(UUID userId);

    void save(RefreshToken refreshToken);

    void removeRefreshToken(String userId);
}
