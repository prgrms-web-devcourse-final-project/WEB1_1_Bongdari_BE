package com.somemore.global.auth.jwt.generator;

import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.domain.TokenType;

public interface JwtGenerator {
    EncodedToken generateToken(String userId, String role, TokenType tokenType);
}
