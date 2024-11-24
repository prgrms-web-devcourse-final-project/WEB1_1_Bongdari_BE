package com.somemore.auth.jwt.validator;

import com.somemore.auth.jwt.domain.EncodedToken;
import com.somemore.auth.jwt.parser.DefaultJwtParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class DefaultJwtValidator implements JwtValidator {

    private final DefaultJwtParser defaultJwtParser;

    public boolean validateToken(EncodedToken token) {
        return defaultJwtParser.parseToken(token)
                .getExpiration()
                .toInstant()
                .isAfter(Instant.now());
    }
}
