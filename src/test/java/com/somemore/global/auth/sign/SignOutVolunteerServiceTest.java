package com.somemore.global.auth.sign;

import com.somemore.global.auth.authentication.UserIdentity;
import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.domain.TokenType;
import com.somemore.global.auth.jwt.exception.JwtErrorType;
import com.somemore.global.auth.jwt.exception.JwtException;
import com.somemore.global.auth.jwt.generator.JwtGenerator;
import com.somemore.global.auth.jwt.domain.RefreshToken;
import com.somemore.global.auth.jwt.manager.TokenManager;
import com.somemore.global.auth.sign.out.SignOutService;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.user.domain.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class SignOutServiceTest extends IntegrationTestSupport {

    @Autowired
    private SignOutService signOutVolunteerService;
    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private MockHttpServletResponse response;

    private final UUID userId = UUID.randomUUID();
    private final UUID roleId = UUID.randomUUID();
    private final UserRole role = UserRole.VOLUNTEER;
    private final UserIdentity userIdentity = UserIdentity.of(userId, roleId, role);;

    @BeforeEach
    void setUp() {
        response = new MockHttpServletResponse();
    }

    @AfterEach
    void tearDown() {
        redisTemplate.keys("*")
                .forEach(redisTemplate::delete);
    }

    @Test
    @DisplayName("로그아웃 시 리프레시 토큰을 제거해야 한다.")
    void signOutDeletesTokens() {
        // Given
        EncodedToken accessToken = jwtGenerator.generateToken(userIdentity, TokenType.ACCESS);

        RefreshToken refreshToken = new RefreshToken(
                userId.toString(),
                accessToken,
                jwtGenerator.generateToken(userIdentity, TokenType.REFRESH));

        tokenManager.save(refreshToken);

        // When
        signOutVolunteerService.signOut(response, userId);

        // Then
        assertThatThrownBy(() -> tokenManager.getRefreshTokenByAccessToken(accessToken))
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
