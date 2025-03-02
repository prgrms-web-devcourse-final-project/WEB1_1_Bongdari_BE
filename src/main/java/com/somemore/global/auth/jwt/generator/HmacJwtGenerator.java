package com.somemore.global.auth.jwt.generator;

import com.somemore.global.auth.authentication.UserIdentity;
import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.domain.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HmacJwtGenerator implements JwtGenerator {

    public static final MacAlgorithm ALGORITHM = Jwts.SIG.HS256;
    private final SecretKey secretKey;

    public EncodedToken generateToken(UserIdentity userIdentity, TokenType tokenType) {
        Claims claims = buildClaims(userIdentity);
        Instant now = Instant.now();
        Instant expiration = now.plusMillis(tokenType.getPeriodInMillis());
        String uniqueId = UUID.randomUUID().toString(); // JTI

        return EncodedToken.from(
                Jwts.builder()
                        .claims(claims)
                        .id(uniqueId)
                        .issuedAt(Date.from(now))
                        .expiration(Date.from(expiration))
                        .signWith(secretKey, ALGORITHM)
                        .compact()
        );
    }

    private static Claims buildClaims(UserIdentity userIdentity) {
        return Jwts.claims()
                .add("userId", userIdentity.userId())
                .add("roleId", userIdentity.roleId())
                .add("role", userIdentity.role().getAuthority())
                .build();
    }
}
