package com.somemore.auth.jwt.refresh.refresher;

import com.somemore.auth.jwt.domain.EncodedToken;

public interface JwtRefresher {
    EncodedToken refreshAccessToken(EncodedToken accessToken);
}
