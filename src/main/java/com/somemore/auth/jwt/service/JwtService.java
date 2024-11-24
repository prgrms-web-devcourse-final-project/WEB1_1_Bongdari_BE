package com.somemore.auth.jwt.service;

import com.somemore.auth.jwt.domain.EncodedToken;
import com.somemore.auth.jwt.domain.TokenType;
import com.somemore.auth.jwt.generator.JwtGenerator;
import com.somemore.auth.jwt.parser.JwtParser;
import com.somemore.auth.jwt.refresh.refresher.JwtRefresher;
import com.somemore.auth.jwt.usecase.JwtUseCase;
import com.somemore.auth.jwt.validator.JwtValidator;
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
    public EncodedToken generateToken(String userId, String role, TokenType tokenType) {
        return jwtGenerator.generateToken(userId, role, tokenType);
    }

    @Override
    public void verifyToken(EncodedToken token) {
        if (jwtValidator.validateToken(token)) {
            return;
        }
        EncodedToken accessToken = jwtRefresher.refreshAccessToken(token);
        // TODO Security Context (JwtFilter) 구현 시 setCookie(accessToken) 구체화
    }

    @Override
    public String getClaimByKey(EncodedToken token, String key) {
        return jwtParser.parseToken(token).get(key, String.class);
    }
}
