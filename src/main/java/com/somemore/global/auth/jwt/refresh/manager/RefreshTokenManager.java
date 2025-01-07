package com.somemore.global.auth.jwt.refresh.manager;

import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.refresh.domain.RefreshToken;

import java.util.UUID;

public interface RefreshTokenManager {
    RefreshToken findRefreshTokenByAccessToken(EncodedToken accessToken);

    RefreshToken findRefreshTokenByUserId(UUID userId);

    void save(RefreshToken refreshToken);

    void removeRefreshToken(String userId);
}
