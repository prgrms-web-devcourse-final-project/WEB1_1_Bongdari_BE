package com.somemore.global.auth.cookie;

import com.somemore.global.auth.jwt.domain.TokenType;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class CookieServiceTest {

    private final CookieService cookieService = new CookieService();

    @Test
    void setAccessToken_ShouldSetCookie() {
        // Given
        MockHttpServletResponse response = new MockHttpServletResponse();
        String accessToken = "test-access-token";

        // When
        cookieService.setToken(response, accessToken, TokenType.ACCESS);

        // Then
        String setCookieHeader = response.getHeader("Set-Cookie");
        assertThat(setCookieHeader).contains("ACCESS_TOKEN=" + accessToken);
        assertThat(setCookieHeader).contains("HttpOnly");
        assertThat(setCookieHeader).contains("Secure");
        assertThat(setCookieHeader).contains("Path=/");
    }

    @Test
    void deleteAccessToken_ShouldRemoveCookie() {
        // Given
        MockHttpServletResponse response = new MockHttpServletResponse();

        // When
        cookieService.deleteAccessToken(response);

        // Then
        String setCookieHeader = response.getHeader("Set-Cookie");
        assertThat(setCookieHeader).contains("ACCESS_TOKEN=" + TokenType.SIGN_OUT.name()); // 빈 값
        assertThat(setCookieHeader).contains("Max-Age=0"); // 삭제
        assertThat(setCookieHeader).contains("Path=/");
    }
}
