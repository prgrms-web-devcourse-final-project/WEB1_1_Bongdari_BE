package com.somemore.global.auth.jwt.refresh.refresher;

import com.somemore.global.auth.jwt.domain.EncodedToken;

public interface JwtRefresher {
    EncodedToken refreshAccessToken(EncodedToken accessToken);
}
