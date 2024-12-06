package com.somemore.auth.cookie;

import com.somemore.auth.jwt.domain.TokenType;
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
    public void setAccessToken(HttpServletResponse response, String value) {
        ResponseCookie cookie = generateCookie(TokenType.ACCESS, value);
        response.addHeader("Set-Cookie", cookie.toString());
        log.info("SET_COOKIE_ACCESS_TOKEN = {}", value);
    }

    @Override
    public void deleteAccessToken(HttpServletResponse response) {
        ResponseCookie cookie = generateCookie(TokenType.SIGNOUT, TokenType.SIGNOUT.name());
        response.addHeader("Set-Cookie", cookie.toString());
        log.info("DELETE_COOKIE_ACCESS_TOKEN");
    }

    private static ResponseCookie generateCookie(TokenType tokenType, String value) {
        return ResponseCookie.from(TokenType.ACCESS.name(), value) // 덮어쓰기 위해서 고정 값
                .domain(".somemore.site")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(tokenType.getPeriodInSeconds())
                .sameSite("None")
                .build();
    }
}
