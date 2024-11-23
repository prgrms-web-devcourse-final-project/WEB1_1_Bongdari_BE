package com.somemore.auth.jwt.parser;

import com.somemore.auth.jwt.domain.EncodedToken;
import io.jsonwebtoken.Claims;

public interface JwtParser {
    Claims parseToken(EncodedToken token);
}
