package com.somemore.global.auth.jwt.generator;

import com.somemore.global.auth.authentication.UserIdentity;
import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.domain.TokenType;

public interface JwtGenerator {

    EncodedToken generateToken(UserIdentity userIdentity, TokenType tokenType);
}
