package com.somemore.global.auth.jwt.usecase;

import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.domain.UserRole;

import java.util.UUID;

public interface GenerateTokensOnLoginUseCase {
    EncodedToken saveRefreshTokenAndReturnAccessToken(UUID userId, UserRole role);
}
