package com.somemore.auth.jwt.usecase;

import com.somemore.auth.jwt.domain.EncodedToken;
import com.somemore.auth.jwt.domain.UserRole;

import java.util.UUID;

public interface GenerateTokensOnLoginUseCase {
    EncodedToken saveRefreshTokenAndReturnAccessToken(UUID userId, UserRole role);
}
