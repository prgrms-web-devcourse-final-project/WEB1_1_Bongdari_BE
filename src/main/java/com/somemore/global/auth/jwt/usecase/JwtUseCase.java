package com.somemore.global.auth.jwt.usecase;

import com.somemore.global.auth.authentication.UserIdentity;
import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.domain.TokenType;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;

public interface JwtUseCase {

    EncodedToken generateToken(UserIdentity userIdentity, TokenType tokenType);

    void validateAccessToken(EncodedToken accessToken, HttpServletResponse response);

    Claims getClaims(EncodedToken token);
}
