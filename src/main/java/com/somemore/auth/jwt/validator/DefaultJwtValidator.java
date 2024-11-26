package com.somemore.auth.jwt.validator;

import com.somemore.auth.jwt.domain.EncodedToken;
import com.somemore.auth.jwt.exception.JwtErrorType;
import com.somemore.auth.jwt.exception.JwtException;
import com.somemore.auth.jwt.parser.DefaultJwtParser;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class DefaultJwtValidator implements JwtValidator {

    private final DefaultJwtParser defaultJwtParser;

    @Override
    public void validateToken(EncodedToken token) {
        Claims claims = defaultJwtParser.parseToken(token);
        validateExpiration(claims);
    }

    private void validateExpiration(Claims claims) {
        if (claims.getExpiration() == null || claims.getExpiration().toInstant().isBefore(Instant.now())) {
            throw new JwtException(JwtErrorType.EXPIRED_TOKEN);
        }
    }
}
