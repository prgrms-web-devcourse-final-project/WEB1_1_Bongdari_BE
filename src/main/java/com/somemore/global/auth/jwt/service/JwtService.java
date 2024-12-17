package com.somemore.global.auth.jwt.service;

import com.somemore.global.auth.cookie.CookieUseCase;
import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.domain.TokenType;
import com.somemore.global.auth.jwt.exception.JwtErrorType;
import com.somemore.global.auth.jwt.exception.JwtException;
import com.somemore.global.auth.jwt.generator.JwtGenerator;
import com.somemore.global.auth.jwt.parser.JwtParser;
import com.somemore.global.auth.jwt.refresh.refresher.JwtRefresher;
import com.somemore.global.auth.jwt.usecase.JwtUseCase;
import com.somemore.global.auth.jwt.validator.JwtValidator;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService implements JwtUseCase {
    private final JwtGenerator jwtGenerator;
    private final JwtParser jwtParser;
    private final JwtValidator jwtValidator;
    private final JwtRefresher jwtRefresher;
    private final CookieUseCase cookieUseCase;

    @Override
    public EncodedToken generateToken(String userId, String role, TokenType tokenType) {
        return jwtGenerator.generateToken(userId, role, tokenType);
    }

    @Override
    public void processAccessToken(EncodedToken accessToken, HttpServletResponse response) {
        try {
            jwtValidator.validateToken(accessToken);
        } catch (JwtException e) {
            handleJwtExpiredException(e, accessToken, response);
        }
    }

    @Override
    public Claims getClaims(EncodedToken token) {
        return jwtParser.parseToken(token);
    }

    private void handleJwtExpiredException(JwtException e, EncodedToken accessToken, HttpServletResponse response) {
        if (e.getErrorType() == JwtErrorType.EXPIRED_TOKEN) {
            EncodedToken refreshedToken = jwtRefresher.refreshAccessToken(accessToken);
            cookieUseCase.setAccessToken(response, refreshedToken.value());
            return;
        }
        throw e;
    }
}
