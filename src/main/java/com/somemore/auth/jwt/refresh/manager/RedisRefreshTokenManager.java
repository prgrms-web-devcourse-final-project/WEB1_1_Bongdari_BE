package com.somemore.auth.jwt.refresh.manager;

import com.somemore.auth.jwt.domain.EncodedToken;
import com.somemore.auth.jwt.exception.JwtErrorType;
import com.somemore.auth.jwt.exception.JwtException;
import com.somemore.auth.jwt.refresh.domain.RefreshToken;
import com.somemore.auth.jwt.refresh.repository.RefreshTokenRepository;
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
    public void removeRefreshToken(EncodedToken accessToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(accessToken.value())
                .orElseThrow(() -> new JwtException(JwtErrorType.EXPIRED_TOKEN));

        refreshTokenRepository.delete(refreshToken);
    }
}
