package com.somemore.volunteer.service;

import com.somemore.IntegrationTestSupport;
import com.somemore.auth.cookie.CookieUseCase;
import com.somemore.auth.jwt.domain.EncodedToken;
import com.somemore.auth.jwt.domain.TokenType;
import com.somemore.auth.jwt.exception.JwtErrorType;
import com.somemore.auth.jwt.exception.JwtException;
import com.somemore.auth.jwt.filter.UserRole;
import com.somemore.auth.jwt.generator.JwtGenerator;
import com.somemore.auth.jwt.refresh.domain.RefreshToken;
import com.somemore.auth.jwt.refresh.manager.RefreshTokenManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

class SignOutVolunteerServiceTest extends IntegrationTestSupport {

    @Autowired
    private SignOutVolunteerService signOutVolunteerService;
    @Autowired
    private CookieUseCase cookieUseCase;
    @Autowired
    private RefreshTokenManager refreshTokenManager;
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private MockHttpServletResponse response;
    private String volunteerId;
    private UserRole role;


    @BeforeEach
    void setUp() {
        response = new MockHttpServletResponse();
        volunteerId = "test-volunteer";
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
        EncodedToken accessToken = jwtGenerator.generateToken(volunteerId, role.name(), TokenType.ACCESS);

        RefreshToken refreshToken = new RefreshToken(
                volunteerId,
                accessToken,
                jwtGenerator.generateToken(volunteerId, role.name(), TokenType.REFRESH));

        refreshTokenManager.save(refreshToken);
        cookieUseCase.setAccessToken(response, accessToken.value());

        // When
        signOutVolunteerService.signOut(response, volunteerId);

        // Then
        assertThatThrownBy(() -> refreshTokenManager.findRefreshToken(accessToken))
                .isInstanceOf(JwtException.class)
                .hasMessage(JwtErrorType.EXPIRED_TOKEN.getMessage());

        assertThat(Arrays.toString(response.getCookies())).contains(TokenType.SIGNOUT.name());
    }

    @Test
    @DisplayName("로그아웃 시 리프레시 토큰이 없어도 예외가 발생하지 않는다.")
    void signOutWithoutRefreshToken() {
        // When
        signOutVolunteerService.signOut(response, volunteerId);

        // Then
        assertThatNoException().isThrownBy(() -> signOutVolunteerService.signOut(response, volunteerId));
        assertThat(Arrays.toString(response.getCookies())).contains(TokenType.SIGNOUT.name());
    }
}