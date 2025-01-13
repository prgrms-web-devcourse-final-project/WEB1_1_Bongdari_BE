package com.somemore.global.auth.usecase;

import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.user.domain.UserRole;

import java.util.UUID;

public interface AuthQueryUseCase {
    EncodedToken getAccessTokenByUserId(UUID userId);
}
