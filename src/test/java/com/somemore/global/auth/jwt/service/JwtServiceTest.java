package com.somemore.global.auth.jwt.service;

import com.somemore.global.auth.authentication.UserIdentity;
import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.domain.RefreshToken;
import com.somemore.global.auth.jwt.domain.TokenType;
import com.somemore.global.auth.jwt.exception.JwtErrorType;
import com.somemore.global.auth.jwt.exception.JwtException;
import com.somemore.global.auth.jwt.manager.TokenManager;
import com.somemore.global.auth.jwt.validator.JwtValidator;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.user.domain.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class JwtServiceTest extends IntegrationTestSupport {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private JwtValidator jwtValidator;
    @Autowired
    private SecretKey secretKey;
    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final UUID userId = UUID.randomUUID();
    private final UUID roleId = UUID.randomUUID();
    private final UserRole role = UserRole.VOLUNTEER;
    private final UserIdentity userIdentity = UserIdentity.of(userId, roleId, role);;

    @AfterEach
    void tearDown() {
        redisTemplate.keys("*")
                .forEach(redisTemplate::delete);
    }

    @DisplayName("액세스 토큰이 올바르게 생성된다")
    @Test
    void generateAndValidateAccessToken() {
        // given

        // when
        EncodedToken token = jwtService.generateToken(userIdentity, TokenType.ACCESS);

        // then
        Claims claims = jwtService.getClaims(token);
        assertThat(claims.get("userId", String.class)).isEqualTo(userId.toString());
        assertThat(claims.get("roleId", String.class)).isEqualTo(roleId.toString());
        assertThat(claims.get("role", String.class)).isEqualTo(role.getAuthority());
        assertThat(claims.getExpiration()).isNotNull();
    }

    @DisplayName("로그인 토큰이 올바르게 생성된다")
    @Test
    void generateAndValidateLoginToken() {
        // given

        // when
        EncodedToken token = jwtService.generateToken(userIdentity, TokenType.SIGN_IN);

        // then
        Claims claims = jwtService.getClaims(token);
        assertThat(claims.get("userId", String.class)).isEqualTo(userId.toString());
        assertThat(claims.get("roleId", String.class)).isEqualTo(roleId.toString());
        assertThat(claims.get("role", String.class)).isEqualTo(role.getAuthority());
        assertThat(claims.getExpiration()).isNotNull();
    }

    @DisplayName("토큰 만료 기간이 정확히 설정되어야 한다")
    @Test
    void tokenExpirationPeriodIsExact() {
        // given

        // when
        EncodedToken accessToken = jwtService.generateToken(userIdentity, TokenType.ACCESS);
        EncodedToken refreshToken = jwtService.generateToken(userIdentity, TokenType.REFRESH);

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

        // when
        EncodedToken token1 = jwtService.generateToken(userIdentity, TokenType.ACCESS);
        EncodedToken token2 = jwtService.generateToken(userIdentity, TokenType.ACCESS);

        // then
        assertThat(token1.value()).isNotEqualTo(token2.value());
    }

    @DisplayName("만료된 엑세스 토큰은 리프레시 토큰이 유효하지 않다면 갱신되지 않고 예외가 발생한다")
    @Test
    void throwExceptionWhenRefreshTokenIsInvalid() {
        // given
        EncodedToken expiredAccessToken = createExpiredToken(userId.toString(), role);
        EncodedToken expiredRefreshToken = createExpiredToken(userId.toString(), role);

        RefreshToken refreshToken = new RefreshToken(userId.toString(), expiredAccessToken, expiredRefreshToken);
        tokenManager.save(refreshToken);

        // when
        // then
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        assertThatThrownBy(() -> jwtService.validateAccessToken(expiredAccessToken, mockResponse))
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

        assertThatThrownBy(() -> jwtService.validateAccessToken(expiredAccessToken, mockResponse))
                .isInstanceOf(JwtException.class)
                .hasMessage(JwtErrorType.EXPIRED_TOKEN.getMessage());
    }

    @DisplayName("기존 RefreshToken이 갱신된다")
    @Test
    void refreshTokenIsUpdated() {
        // given
        String userId = UUID.randomUUID().toString();
        UserRole role = UserRole.VOLUNTEER;

        EncodedToken expiredAccessToken = createExpiredToken(userId, role);
        RefreshToken oldRefreshToken = createAndSaveRefreshToken(userId, expiredAccessToken, Instant.now().plusMillis(TokenType.REFRESH.getPeriodInMillis()));

        EncodedToken newAccessToken = jwtService.generateToken(userIdentity, TokenType.ACCESS);
        RefreshToken newRefreshToken = createAndSaveRefreshToken(userId, newAccessToken, Instant.now().plusMillis(TokenType.REFRESH.getPeriodInMillis()));

        // when
        // then
        assertThatThrownBy(() -> tokenManager.getRefreshTokenByAccessToken(expiredAccessToken))
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
        EncodedToken encodedToken = EncodedToken.from(invalidToken);

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
        assertThatThrownBy(() -> jwtService.validateAccessToken(expiredAccessToken, new MockHttpServletResponse()))
                .isInstanceOf(JwtException.class)
                .hasMessage(JwtErrorType.EXPIRED_TOKEN.getMessage());
    }

    private EncodedToken createExpiredToken(String userId, UserRole role) {
        Claims claims = buildClaims(userId, role);

        Instant now = Instant.now();
        Instant expiration = now.plusMillis(-1); // 과거

        return EncodedToken.from(Jwts.builder()
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
                EncodedToken.from(Jwts.builder()
                        .claims(claims)
                        .id(uniqueId)
                        .issuedAt(Date.from(now))
                        .expiration(Date.from(expiration))
                        .signWith(secretKey, Jwts.SIG.HS256)
                        .compact()));

        tokenManager.save(refreshToken);

        return refreshToken;
    }

    private Claims buildClaims(String userId, UserRole role) {
        return Jwts.claims()
                .add("id", userId)
                .add("role", role)
                .build();
    }

}
