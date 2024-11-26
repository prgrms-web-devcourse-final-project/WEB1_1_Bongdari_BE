package com.somemore.auth.jwt.refresh.refresher;

import com.somemore.auth.jwt.domain.EncodedToken;
import com.somemore.auth.jwt.domain.TokenType;
import com.somemore.auth.jwt.generator.JwtGenerator;
import com.somemore.auth.jwt.parser.JwtParser;
import com.somemore.auth.jwt.refresh.domain.RefreshToken;
import com.somemore.auth.jwt.refresh.manager.RefreshTokenManager;
import com.somemore.auth.jwt.validator.JwtValidator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultJwtRefresher implements JwtRefresher {

    private final RefreshTokenManager refreshTokenManager;
    private final JwtValidator jwtValidator;
    private final JwtParser jwtParser;
    private final JwtGenerator jwtGenerator;

    @Override
    public EncodedToken refreshAccessToken(EncodedToken accessToken) {
        RefreshToken refreshToken = refreshTokenManager.findRefreshToken(accessToken);
        EncodedToken refreshTokenValue = new EncodedToken(refreshToken.getRefreshToken());
        jwtValidator.validateToken(refreshTokenValue);

        Claims claims = jwtParser.parseToken(refreshTokenValue);
        refreshToken.updateAccessToken(generateAccessToken(claims));
        refreshTokenManager.save(refreshToken);

        return new EncodedToken(refreshToken.getAccessToken());
    }

    private EncodedToken generateAccessToken(Claims claims) {
        return jwtGenerator.generateToken(
                claims.get("id", String.class),
                claims.get("role", String.class),
                TokenType.ACCESS
        );
    }
}
