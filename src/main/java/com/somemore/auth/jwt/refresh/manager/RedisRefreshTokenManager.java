package com.somemore.auth.jwt.refresh.manager;

import com.somemore.auth.jwt.domain.EncodedToken;
import com.somemore.auth.jwt.refresh.domain.RefreshToken;
import com.somemore.auth.jwt.refresh.repository.RefreshTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisRefreshTokenManager implements RefreshTokenManager {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken findRefreshToken(EncodedToken accessToken) {
        return refreshTokenRepository.findByAccessToken(accessToken.value())
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void save(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    // TODO 로그아웃에 사용
    @Override
    public void removeRefreshToken(EncodedToken accessToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(accessToken.value())
                .orElseThrow(EntityNotFoundException::new);

        refreshTokenRepository.delete(refreshToken);
    }
}
