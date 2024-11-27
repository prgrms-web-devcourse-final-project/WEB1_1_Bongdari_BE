package com.somemore.auth.jwt.refresh.manager;

import com.somemore.auth.jwt.domain.EncodedToken;
import com.somemore.auth.jwt.refresh.domain.RefreshToken;

public interface RefreshTokenManager {
    RefreshToken findRefreshToken(EncodedToken accessToken);

    void save(RefreshToken refreshToken);

    void removeRefreshToken(String userId);
}
