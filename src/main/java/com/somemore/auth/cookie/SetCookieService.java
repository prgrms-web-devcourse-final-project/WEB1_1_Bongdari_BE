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
public class SetCookieService implements SetCookieUseCase {

    @Override
    public void setToken(HttpServletResponse response, String value, TokenType tokenType) {
        ResponseCookie cookie = generateCookie(tokenType.name(), value, tokenType.getPeriodInSeconds());
        response.addHeader("Set-Cookie", cookie.toString());
    }

    private static ResponseCookie generateCookie(String name, String value, int time) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(time)
                .sameSite("Lax")
                .build();
    }
}
