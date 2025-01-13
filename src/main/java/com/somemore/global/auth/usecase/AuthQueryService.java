package com.somemore.global.auth.usecase;

import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.refresh.manager.RefreshTokenManager;
import com.somemore.user.domain.UserRole;
import com.somemore.user.usecase.UserQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthQueryService implements AuthQueryUseCase {

    private final RefreshTokenManager refreshTokenManager;

    @Override
    public EncodedToken getAccessTokenByUserId(UUID userId) {
        return new EncodedToken(
                refreshTokenManager.findRefreshTokenByUserId(userId)
                        .getAccessToken());
    }
}
