package com.somemore.global.auth.jwt.parser;

import com.somemore.global.auth.jwt.domain.EncodedToken;
import io.jsonwebtoken.Claims;

public interface JwtParser {
    Claims parseToken(EncodedToken token);
}
