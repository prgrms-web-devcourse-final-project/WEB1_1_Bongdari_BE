package com.somemore.auth.jwt.generator;

import com.somemore.auth.jwt.domain.EncodedToken;
import com.somemore.auth.jwt.domain.TokenType;

public interface JwtGenerator {
    EncodedToken generateToken(String userId, String role, TokenType tokenType);
}
