package com.somemore.global.auth.jwt.service;

import com.somemore.global.auth.authentication.UserIdentity;
import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.domain.TokenType;
import com.somemore.global.auth.jwt.exception.JwtErrorType;
import com.somemore.global.auth.jwt.exception.JwtException;
import com.somemore.global.auth.jwt.generator.JwtGenerator;
import com.somemore.global.auth.jwt.parser.JwtParser;
import com.somemore.global.auth.jwt.refresher.JwtRefresher;
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

    @Override
    public EncodedToken generateToken(UserIdentity userIdentity, TokenType tokenType) {
        return jwtGenerator.generateToken(userIdentity, tokenType);
    }

    @Override
    public void validateAccessToken(EncodedToken accessToken, HttpServletResponse response) {
        jwtValidator.validateToken(accessToken);
    }

    @Override
    public Claims getClaims(EncodedToken accessToken) {
        return jwtParser.parseToken(accessToken);
    }

    private void handleJwtExpiredException(JwtException e, EncodedToken accessToken, HttpServletResponse response) {
        if (e.getErrorType() == JwtErrorType.EXPIRED_TOKEN) {
            EncodedToken refreshedToken = jwtRefresher.refreshAccessToken(accessToken);
            // TODO 프론트엔드와 협의 : 만료된 액세스 토큰 관리 방법
            return;
        }
        throw e;
    }
}
