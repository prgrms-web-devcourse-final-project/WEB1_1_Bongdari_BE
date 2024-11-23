package com.somemore.auth.jwt.validator;

import com.somemore.auth.jwt.domain.EncodedToken;

public interface JwtValidator {
    boolean validateToken(EncodedToken token);
}
