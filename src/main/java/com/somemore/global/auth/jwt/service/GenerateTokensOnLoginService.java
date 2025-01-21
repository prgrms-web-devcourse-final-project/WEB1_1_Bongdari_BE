package com.somemore.global.auth.jwt.service;

import com.somemore.global.auth.authentication.UserIdentity;
import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.domain.RefreshToken;
import com.somemore.global.auth.jwt.domain.TokenType;
import com.somemore.global.auth.jwt.generator.JwtGenerator;
import com.somemore.global.auth.jwt.manager.TokenManager;
import com.somemore.global.auth.jwt.usecase.GenerateTokensOnLoginUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GenerateTokensOnLoginService implements GenerateTokensOnLoginUseCase {

    private final JwtGenerator jwtGenerator;
    private final TokenManager tokenManager;

    @Override
    public EncodedToken generateLoginToken(UserIdentity userIdentity) {
        return generateToken(userIdentity, TokenType.SIGN_IN);
    }

    @Override
    public EncodedToken generateAuthTokensAndReturnAccessToken(UserIdentity userIdentity) {
        EncodedToken accessToken = generateToken(userIdentity, TokenType.ACCESS);
        RefreshToken refreshToken = generateRefreshTokenWithAccessToken(userIdentity, accessToken);
        saveRefreshToken(refreshToken);

        return accessToken;
    }

    private EncodedToken generateToken(UserIdentity userIdentity, TokenType tokenType) {
        return jwtGenerator.generateToken(userIdentity, tokenType);
    }

    private RefreshToken generateRefreshTokenWithAccessToken(UserIdentity userIdentity, EncodedToken accessToken) {
        EncodedToken refreshToken = generateToken(userIdentity, TokenType.REFRESH);

        return new RefreshToken(
                userIdentity.userId().toString(),
                accessToken,
                refreshToken
        );
    }

    private void saveRefreshToken(RefreshToken refreshToken) {
        tokenManager.save(refreshToken);
    }
}
