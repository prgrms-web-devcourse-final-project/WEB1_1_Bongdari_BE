package com.somemore.global.auth.jwt.validator;

import com.somemore.global.auth.jwt.domain.EncodedToken;

public interface JwtValidator {
    void validateToken(EncodedToken token);
}
