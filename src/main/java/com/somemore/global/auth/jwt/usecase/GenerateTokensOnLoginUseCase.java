package com.somemore.global.auth.jwt.usecase;

import com.somemore.global.auth.authentication.UserIdentity;
import com.somemore.global.auth.jwt.domain.EncodedToken;

public interface GenerateTokensOnLoginUseCase {
    EncodedToken generateLoginToken(UserIdentity userIdentity);

    EncodedToken generateAuthTokensAndReturnAccessToken(UserIdentity userIdentity);
}
