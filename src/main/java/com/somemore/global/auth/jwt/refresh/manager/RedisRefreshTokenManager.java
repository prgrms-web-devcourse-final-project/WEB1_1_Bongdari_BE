package com.somemore.global.auth.jwt.refresh.manager;

import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.exception.JwtErrorType;
import com.somemore.global.auth.jwt.exception.JwtException;
import com.somemore.global.auth.jwt.refresh.domain.RefreshToken;
import com.somemore.global.auth.jwt.refresh.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisRefreshTokenManager implements RefreshTokenManager {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken findRefreshToken(EncodedToken accessToken) {
        return refreshTokenRepository.findByAccessToken(accessToken.value())
                .orElseThrow(() -> new JwtException(JwtErrorType.EXPIRED_TOKEN));
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
