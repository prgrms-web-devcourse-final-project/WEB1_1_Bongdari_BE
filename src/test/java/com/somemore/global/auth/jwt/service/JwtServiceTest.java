package com.somemore.global.auth.jwt.service;

import com.somemore.support.IntegrationTestSupport;
import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.domain.TokenType;
import com.somemore.user.domain.UserRole;
import com.somemore.global.auth.jwt.exception.JwtErrorType;
import com.somemore.global.auth.jwt.exception.JwtException;
import com.somemore.global.auth.jwt.refresh.domain.RefreshToken;
import com.somemore.global.auth.jwt.refresh.manager.RefreshTokenManager;
import com.somemore.global.auth.jwt.validator.JwtValidator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;


class JwtServiceTest extends IntegrationTestSupport {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private JwtValidator jwtValidator;
    @Autowired
    private SecretKey secretKey;
    @Autowired
    private RefreshTokenManager refreshTokenManager;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @AfterEach
    void tearDown() {
        redisTemplate.keys("*")
                .forEach(redisTemplate::delete);
    }

    @DisplayName("액세스 토큰이 올바르게 생성된다")
    @Test
    void generateAndValidateAccessToken() {
        // given
        String userId = UUID.randomUUID().toString();
        UserRole role = UserRole.VOLUNTEER;
        TokenType tokenType = TokenType.ACCESS;

        // when
        EncodedToken token = jwtService.generateToken(userId, role.getAuthority(), tokenType);

        // then
        Claims claims = jwtService.getClaims(token);
        assertThat(claims.get("id", String.class)).isEqualTo(userId);
        assertThat(claims.get("role", String.class)).isEqualTo(role.getAuthority());
        assertThat(claims.getExpiration()).isNotNull();
    }

    @DisplayName("로그인 토큰이 올바르게 생성된다")
    @Test
    void generateAndValidateLoginToken() {
        // given
        String userId = UUID.randomUUID().toString();
        UserRole role = UserRole.VOLUNTEER;
        TokenType tokenType = TokenType.ACCESS;

        // when
        EncodedToken token = jwtService.generateToken(userId, role.getAuthority(), tokenType);

        // then
        Claims claims = jwtService.getClaims(token);
        assertThat(claims.get("id", String.class)).isEqualTo(userId);
        assertThat(claims.get("role", String.class)).isEqualTo(role.getAuthority());
        assertThat(claims.getExpiration()).isNotNull();
    }

    @DisplayName("토큰 만료 기간이 정확히 설정되어야 한다")
    @Test
    void tokenExpirationPeriodIsExact() {
        // given
        String userId = UUID.randomUUID().toString();
        UserRole role = UserRole.VOLUNTEER;

        // when
        EncodedToken accessToken = jwtService.generateToken(userId, role.getAuthority(), TokenType.ACCESS);
        EncodedToken refreshToken = jwtService.generateToken(userId, role.getAuthority(), TokenType.REFRESH);

        // then
        Claims accessClaims = jwtService.getClaims(accessToken);
        Claims refreshClaims = jwtService.getClaims(refreshToken);

        long accessTokenDuration = accessClaims.getExpiration().getTime() - accessClaims.getIssuedAt().getTime();
        long refreshTokenDuration = refreshClaims.getExpiration().getTime() - refreshClaims.getIssuedAt().getTime();

        assertThat(accessTokenDuration).isEqualTo(TokenType.ACCESS.getPeriodInMillis());
        assertThat(refreshTokenDuration).isEqualTo(TokenType.REFRESH.getPeriodInMillis());
    }

    @DisplayName("동일한 사용자로 여러 토큰 생성 시 서로 다른 값이어야 한다")
    @Test
    void multipleTokensForSameUserAreDifferent() {
        // given
        String userId = UUID.randomUUID().toString();
        UserRole role = UserRole.VOLUNTEER;

        // when
        EncodedToken token1 = jwtService.generateToken(userId, role.getAuthority(), TokenType.ACCESS);
        EncodedToken token2 = jwtService.generateToken(userId, role.getAuthority(), TokenType.ACCESS);

        // then
        assertThat(token1.value()).isNotEqualTo(token2.value());
    }

    @DisplayName("만료된 엑세스 토큰은 리프레시 토큰이 유효하지 않다면 갱신되지 않고 예외가 발생한다")
    @Test
    void throwExceptionWhenRefreshTokenIsInvalid() {
        // given
        String userId = UUID.randomUUID().toString();
        UserRole role = UserRole.VOLUNTEER;
        EncodedToken expiredAccessToken = createExpiredToken(userId, role);

        EncodedToken expiredRefreshToken = createExpiredToken(userId, role);
        RefreshToken refreshToken = new RefreshToken(userId, expiredAccessToken, expiredRefreshToken);
        refreshTokenManager.save(refreshToken);

        // when
        // then
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        assertThatThrownBy(() -> jwtService.processAccessToken(expiredAccessToken, mockResponse))
                .isInstanceOf(JwtException.class)
                .hasMessage(JwtErrorType.EXPIRED_TOKEN.getMessage());
    }

    @DisplayName("만료된 엑세스 토큰은 리프레시 토큰이 존재하지 않는다면 갱신되지 않고 예외가 발생한다")
    @Test
    void throwExceptionWhenRefreshTokenIsMissing() {
        // given
        String userId = UUID.randomUUID().toString();
        UserRole role = UserRole.VOLUNTEER;
        EncodedToken expiredAccessToken = createExpiredToken(userId, role);

        // when
        // then
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        assertThatThrownBy(() -> jwtService.processAccessToken(expiredAccessToken, mockResponse))
                .isInstanceOf(JwtException.class)
                .hasMessage(JwtErrorType.EXPIRED_TOKEN.getMessage());
    }

    @DisplayName("리프레시된 AccessToken은 쿠키에 올바르게 저장된다")
    @Test
    void refreshedAccessTokenIsSetInCookie() {
        // given
        String userId = UUID.randomUUID().toString();
        UserRole role = UserRole.VOLUNTEER;

        EncodedToken expiredAccessToken = createExpiredToken(userId, role);
        createAndSaveRefreshToken(userId, expiredAccessToken, Instant.now().plusMillis(TokenType.REFRESH.getPeriodInMillis()));

        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        // when
        jwtService.processAccessToken(expiredAccessToken, mockResponse);

        // then
        String cookieHeader = mockResponse.getHeader("Set-Cookie");
        assertThat(cookieHeader).contains("ACCESS_TOKEN=");
        assertThat(cookieHeader).contains("HttpOnly");
        assertThat(cookieHeader).contains("Secure");
    }

    @DisplayName("기존 RefreshToken이 갱신된다")
    @Test
    void refreshTokenIsUpdated() {
        // given
        String userId = UUID.randomUUID().toString();
        UserRole role = UserRole.VOLUNTEER;

        EncodedToken expiredAccessToken = createExpiredToken(userId, role);
        RefreshToken oldRefreshToken = createAndSaveRefreshToken(userId, expiredAccessToken, Instant.now().plusMillis(TokenType.REFRESH.getPeriodInMillis()));

        EncodedToken newAccessToken = jwtService.generateToken(userId, role.getAuthority(), TokenType.ACCESS);
        RefreshToken newRefreshToken = createAndSaveRefreshToken(userId, newAccessToken, Instant.now().plusMillis(TokenType.REFRESH.getPeriodInMillis()));

        // when
        // then
        assertThatThrownBy(() -> refreshTokenManager.findRefreshTokenByAccessToken(expiredAccessToken))
                .isInstanceOf(JwtException.class)
                .hasMessage(JwtErrorType.EXPIRED_TOKEN.getMessage());

        assertThat(newRefreshToken.getAccessToken()).isEqualTo(newAccessToken.value());
        assertThat(newRefreshToken.getRefreshToken()).isNotEqualTo(oldRefreshToken.getRefreshToken());
    }


    @DisplayName("잘못된 JWT 토큰은 예외가 발생한다")
    @Test
    void invalidTokenThrowsJwtException() {
        // given
        String invalidToken = "invalid.token.value";
        EncodedToken encodedToken = new EncodedToken(invalidToken);

        // when
        // then
        assertThatThrownBy(() -> jwtValidator.validateToken(encodedToken))
                .isInstanceOf(JwtException.class)
                .hasMessage(JwtErrorType.UNKNOWN_ERROR.getMessage());
    }

    @DisplayName("만료된 JWT 토큰은 예외가 발생한다")
    @Test
    void expiredTokenThrowsJwtException() {
        // given
        String userId = UUID.randomUUID().toString();
        UserRole role = UserRole.VOLUNTEER;
        EncodedToken expiredAccessToken = createExpiredToken(userId, role);

        // when
        // then
        assertThatThrownBy(() -> jwtValidator.validateToken(expiredAccessToken))
                .isInstanceOf(JwtException.class)
                .hasMessage(JwtErrorType.EXPIRED_TOKEN.getMessage());
    }

    @DisplayName("RefreshToken이 존재하지 않으면 예외가 발생한다")
    @Test
    void refreshTokenNotFoundThrowsJwtException() {
        // given
        String userId = UUID.randomUUID().toString();
        UserRole role = UserRole.VOLUNTEER;
        EncodedToken expiredAccessToken = createExpiredToken(userId, role);

        // when
        // then
        assertThatThrownBy(() -> jwtService.processAccessToken(expiredAccessToken, new MockHttpServletResponse()))
                .isInstanceOf(JwtException.class)
                .hasMessage(JwtErrorType.EXPIRED_TOKEN.getMessage());
    }

    private EncodedToken createExpiredToken(String userId, UserRole role) {
        Claims claims = buildClaims(userId, role);

        Instant now = Instant.now();
        Instant expiration = now.plusMillis(-1); // 과거

        return new EncodedToken(Jwts.builder()
                .claims(claims)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact());
    }

    private RefreshToken createAndSaveRefreshToken(String userId, EncodedToken accessToken, Instant expiration) {
        Claims claims = buildClaims(userId, UserRole.VOLUNTEER);
        Instant now = Instant.now();
        String uniqueId = UUID.randomUUID().toString(); // jti

        RefreshToken refreshToken = new RefreshToken(
                userId,
                accessToken,
                new EncodedToken(Jwts.builder()
                        .claims(claims)
                        .id(uniqueId)
                        .issuedAt(Date.from(now))
                        .expiration(Date.from(expiration))
                        .signWith(secretKey, Jwts.SIG.HS256)
                        .compact()));

        refreshTokenManager.save(refreshToken);

        return refreshToken;
    }

    private Claims buildClaims(String userId, UserRole role) {
        return Jwts.claims()
                .add("id", userId)
                .add("role", role)
                .build();
    }

}
