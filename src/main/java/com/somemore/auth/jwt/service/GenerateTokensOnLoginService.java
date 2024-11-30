package com.somemore.auth.jwt.service;

import com.somemore.auth.jwt.domain.UserRole;
import com.somemore.auth.jwt.domain.EncodedToken;
import com.somemore.auth.jwt.domain.TokenType;
import com.somemore.auth.jwt.generator.JwtGenerator;
import com.somemore.auth.jwt.refresh.domain.RefreshToken;
import com.somemore.auth.jwt.refresh.manager.RefreshTokenManager;
import com.somemore.auth.jwt.usecase.GenerateTokensOnLoginUseCase;
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
    public EncodedToken saveRefreshTokenAndReturnAccessToken(UUID volunteerId) {
        EncodedToken accessToken = generateToken(volunteerId, TokenType.ACCESS);
        RefreshToken refreshToken = generateRefreshToken(volunteerId, accessToken);
        saveRefreshToken(refreshToken);

        return accessToken;
    }

    private EncodedToken generateToken(UUID volunteerId, TokenType tokenType) {
        return jwtGenerator.generateToken(
                volunteerId.toString(),
                UserRole.VOLUNTEER.getAuthority(),
                tokenType);
    }

    private RefreshToken generateRefreshToken(UUID volunteerId, EncodedToken accessToken) {
        return new RefreshToken(
                volunteerId.toString(),
                accessToken,
                generateToken(
                        volunteerId,
                        TokenType.REFRESH));
    }

    private void saveRefreshToken(RefreshToken refreshToken) {
        refreshTokenManager.save(refreshToken);
    }
}
