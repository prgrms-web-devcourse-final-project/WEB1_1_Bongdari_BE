package com.somemore.auth.jwt.usecase;

import com.somemore.auth.jwt.domain.EncodedToken;
import com.somemore.auth.jwt.domain.TokenType;

public interface JwtUseCase {
    EncodedToken generateToken(String userId, String role, TokenType tokenType);

    void verifyToken(EncodedToken token);

    String getClaimByKey(EncodedToken token, String key);
}
