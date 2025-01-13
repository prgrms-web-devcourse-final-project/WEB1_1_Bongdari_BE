package com.somemore.global.auth.jwt.manager;

import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.exception.JwtErrorType;
import com.somemore.global.auth.jwt.exception.JwtException;
import com.somemore.global.auth.jwt.domain.RefreshToken;
import com.somemore.global.auth.jwt.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RedisTokenManager implements TokenManager {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken getRefreshTokenByAccessToken(EncodedToken accessToken) {
        return refreshTokenRepository.findByAccessToken(accessToken.value())
                .orElseThrow(() -> new JwtException(JwtErrorType.EXPIRED_TOKEN));
    }

    @Override
    public EncodedToken getAccessTokenByUserId(UUID userId) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId.toString())
                .orElseThrow(() -> new JwtException(JwtErrorType.EXPIRED_TOKEN));

        return EncodedToken.from(refreshToken.getAccessToken());
    }

    @Override
    public void save(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void removeRefreshToken(String userId) {
        refreshTokenRepository.findByUserId(userId)
                .ifPresent(refreshTokenRepository::delete);
    }
}
