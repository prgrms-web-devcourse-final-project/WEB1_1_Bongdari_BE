package com.somemore.auth.jwt.generator;

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

    private static final String ID = "id";
    private static final String ROLE = "role";
    private static final long ACCESS_TOKEN_PERIOD = 1000 * 60 * 60 * 24 * 7; // 1주
    private static final long REFRESH_TOKEN_PERIOD = 1000 * 60 * 30; // 30분

    public static final MacAlgorithm ALGORITHM = Jwts.SIG.HS256;

    private final SecretKey secretKey;

    public String generateToken(String userId, String role) {
        String accessToken = generateToken(userId, role, ACCESS_TOKEN_PERIOD);
        String refreshToken = generateToken(userId, role, REFRESH_TOKEN_PERIOD);

        // TODO tokenService.saveRefreshToken(userId, accessToken, refreshToken);
        return accessToken;
    }

    private String generateToken(String userId, String role, long period) {
        Claims claims = buildClaims(userId, role);
        Instant now = Instant.now();
        Instant expirationInstant = now.plusMillis(period);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expirationInstant))
                .signWith(secretKey, ALGORITHM)
                .compact();
    }

    private static Claims buildClaims(String userId, String role) {
        Claims claims = Jwts.claims().build();
        claims.put(ID, userId);
        claims.put(ROLE, role);
        return claims;
    }
}
