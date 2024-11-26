package com.somemore.auth.jwt.usecase;

import com.somemore.auth.jwt.domain.EncodedToken;
import com.somemore.auth.jwt.domain.TokenType;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;

public interface JwtUseCase {
    EncodedToken generateToken(String userId, String role, TokenType tokenType);

    void processAccessToken(EncodedToken token, HttpServletResponse response);

    Claims getClaims(EncodedToken token);

}
