package com.somemore.global.auth.jwt.usecase;

import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.user.domain.UserRole;

import java.util.UUID;

public interface GenerateTokensOnLoginUseCase {
    EncodedToken generateLoginToken(UUID userId, UserRole role);
    EncodedToken generateAuthTokensAndReturnAccessToken(UUID userId, UserRole role);
}
