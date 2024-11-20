package com.somemore.auth.jwt.parser;

import io.jsonwebtoken.Claims;

public interface JwtParser {
    Claims parseToken(String token);
}
