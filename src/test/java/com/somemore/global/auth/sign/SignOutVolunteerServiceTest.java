package com.somemore.global.auth.sign;

import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.domain.TokenType;
import com.somemore.global.auth.jwt.exception.JwtErrorType;
import com.somemore.global.auth.jwt.exception.JwtException;
import com.somemore.global.auth.jwt.generator.JwtGenerator;
import com.somemore.global.auth.jwt.refresh.domain.RefreshToken;
import com.somemore.global.auth.jwt.refresh.manager.RefreshTokenManager;
import com.somemore.global.auth.signout.service.SignOutService;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.user.domain.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SignOutServiceTest extends IntegrationTestSupport {

    @Autowired
    private SignOutService signOutVolunteerService;
    @Autowired
    private RefreshTokenManager refreshTokenManager;
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private MockHttpServletResponse response;
    private UUID userId;
    private UserRole role;


    @BeforeEach
    void setUp() {
        response = new MockHttpServletResponse();
        userId = UUID.randomUUID();
        role = UserRole.VOLUNTEER;
    }

    @AfterEach
    void tearDown() {
        redisTemplate.keys("*")
                .forEach(redisTemplate::delete);
    }

    @Test
    @DisplayName("로그아웃 시 액세스 토큰 쿠키를 삭제하고 리프레시 토큰을 제거해야 한다.")
    void signOutDeletesTokens() {
        // Given
        EncodedToken accessToken = jwtGenerator.generateToken(userId.toString(), role.getAuthority(), TokenType.ACCESS);

        RefreshToken refreshToken = new RefreshToken(
                userId.toString(),
                accessToken,
                jwtGenerator.generateToken(userId.toString(), role.getAuthority(), TokenType.REFRESH));

        refreshTokenManager.save(refreshToken);

        // When
        signOutVolunteerService.signOut(response, userId);

        // Then
        assertThatThrownBy(() -> refreshTokenManager.findRefreshTokenByAccessToken(accessToken))
                .isInstanceOf(JwtException.class)
                .hasMessage(JwtErrorType.EXPIRED_TOKEN.getMessage());
    }

    @Test
    @DisplayName("로그아웃 시 리프레시 토큰이 없어도 예외가 발생하지 않는다.")
    void signOutWithoutRefreshToken() {
        // When
        signOutVolunteerService.signOut(response, userId);

        // Then
        assertThatNoException().isThrownBy(() -> signOutVolunteerService.signOut(response, userId));
    }
}
