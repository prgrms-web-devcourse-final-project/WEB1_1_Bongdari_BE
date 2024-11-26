package com.somemore.auth.jwt.parser;

import com.somemore.auth.jwt.domain.EncodedToken;
import com.somemore.auth.jwt.exception.JwtErrorType;
import com.somemore.auth.jwt.exception.JwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
public class DefaultJwtParser implements JwtParser {

    private final SecretKey secretKey;

    @Override
    public Claims parseToken(EncodedToken token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token.value())
                    .getPayload();

        } catch (SignatureException e) {
            throw new JwtException(JwtErrorType.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new JwtException(JwtErrorType.EXPIRED_TOKEN);
        } catch (Exception e) {
            throw new JwtException(JwtErrorType.UNKNOWN_ERROR);
        }
    }
}
