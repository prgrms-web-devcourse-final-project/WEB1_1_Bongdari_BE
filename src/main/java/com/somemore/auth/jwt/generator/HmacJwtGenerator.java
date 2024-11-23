package com.somemore.auth.jwt.generator;

import com.somemore.auth.jwt.domain.EncodedToken;
import com.somemore.auth.jwt.domain.TokenType;
import com.somemore.auth.jwt.refresh.manager.RefreshTokenManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class HmacJwtGenerator implements JwtGenerator {

    public static final MacAlgorithm ALGORITHM = Jwts.SIG.HS256;
    private final SecretKey secretKey;

    public EncodedToken generateToken(String userId, String role, TokenType tokenType) {
        Claims claims = buildClaims(userId, role);
        Instant now = Instant.now();
        Instant expiration = now.plusMillis(tokenType.getPeriod());

        return new EncodedToken(Jwts.builder()
                .claims(claims)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(secretKey, ALGORITHM)
                .compact());
    }

    private static Claims buildClaims(String userId, String role) {
        final String ID = "id";
        final String ROLE = "role";

        return Jwts.claims()
                .add(ID, userId)
                .add(ROLE, role)
                .build();
    }
}
