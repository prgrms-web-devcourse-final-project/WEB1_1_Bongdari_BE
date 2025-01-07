package com.somemore.global.auth.jwt.service;

import com.somemore.user.domain.UserRole;
import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.domain.TokenType;
import com.somemore.global.auth.jwt.generator.JwtGenerator;
import com.somemore.global.auth.jwt.refresh.domain.RefreshToken;
import com.somemore.global.auth.jwt.refresh.manager.RefreshTokenManager;
import com.somemore.global.auth.jwt.usecase.GenerateTokensOnLoginUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GenerateTokensOnLoginService implements GenerateTokensOnLoginUseCase {

    private final JwtGenerator jwtGenerator;
    private final RefreshTokenManager refreshTokenManager;

    @Override
    public EncodedToken generateLoginToken(UUID userId, UserRole role) {
        return generateToken(userId, role, TokenType.LOGIN);
    }

    @Override
    public EncodedToken generateAuthTokensAndReturnAccessToken(UUID userId, UserRole role) {
        EncodedToken accessToken = generateToken(userId, role, TokenType.ACCESS);
        RefreshToken refreshToken = generateRefreshTokenWithAccessToken(userId, role, accessToken);
        saveRefreshToken(refreshToken);

        return accessToken;
    }

    private EncodedToken generateToken(UUID userId, UserRole role, TokenType tokenType) {
        return jwtGenerator.generateToken(
                userId.toString(),
                role.getAuthority(),
                tokenType
        );
    }

    private RefreshToken generateRefreshTokenWithAccessToken(UUID userId, UserRole role, EncodedToken accessToken) {
        return new RefreshToken(
                userId.toString(),
                accessToken,
                generateToken(userId, role, TokenType.REFRESH)
        );
    }

    private void saveRefreshToken(RefreshToken refreshToken) {
        refreshTokenManager.save(refreshToken);
    }
}
