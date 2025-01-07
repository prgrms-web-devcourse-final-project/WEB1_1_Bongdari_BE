package com.somemore.global.auth.cookie;

import com.somemore.global.auth.jwt.domain.TokenType;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CookieService implements CookieUseCase {

    @Override
    public void setToken(HttpServletResponse response, String value, TokenType tokenType) {
        ResponseCookie cookie = generateCookie(tokenType, value);
        response.addHeader("Set-Cookie", cookie.toString());
    }

    @Override
    public void deleteAccessToken(HttpServletResponse response) {
        ResponseCookie cookie = generateCookie(TokenType.SIGNOUT, TokenType.SIGNOUT.name());
        response.addHeader("Set-Cookie", cookie.toString());
    }

    private static ResponseCookie generateCookie(TokenType tokenType, String value) {
        return ResponseCookie.from(TokenType.ACCESS.getDescription(), value)
                .domain(".somemore.site")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(tokenType.getPeriodInSeconds())
                .sameSite("None")
                .build();
    }
}
