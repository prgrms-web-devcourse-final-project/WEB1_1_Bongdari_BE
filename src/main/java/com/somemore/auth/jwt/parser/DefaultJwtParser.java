package com.somemore.auth.jwt.parser;

import com.somemore.auth.jwt.domain.EncodedToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
public class DefaultJwtParser implements JwtParser {

    private final SecretKey secretKey;

    public Claims parseToken(EncodedToken token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token.value())
                .getPayload();
    }
}
