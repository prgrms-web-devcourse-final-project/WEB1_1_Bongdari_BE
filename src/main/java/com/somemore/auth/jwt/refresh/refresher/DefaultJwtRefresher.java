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
        validateToken(refreshToken);

        Claims claims = jwtParser.parseToken(accessToken);
        refreshToken.updateAccessToken(generateAccessToken(claims));
        refreshTokenManager.save(refreshToken);

        return new EncodedToken(refreshToken.getAccessToken());
    }

    private void validateToken(RefreshToken refreshToken) {
        if (jwtValidator.validateToken(new EncodedToken(refreshToken.getAccessToken()))) {
            // TODO Security Context (JwtFilter) 구현 시 예외 처리 구체화
            log.error("리프레시 토큰이 만료되었습니다. 로그인을 다시 해야합니다");
            throw new RuntimeException();
        }
    }

    private EncodedToken generateAccessToken(Claims claims) {
        return jwtGenerator.generateToken(
                claims.get("id", String.class),
                claims.get("role", String.class),
                TokenType.ACCESS
        );
    }
}
