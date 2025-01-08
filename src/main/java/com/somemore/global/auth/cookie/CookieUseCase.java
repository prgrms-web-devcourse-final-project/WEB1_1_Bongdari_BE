package com.somemore.global.auth.cookie;

import com.somemore.global.auth.jwt.domain.TokenType;
import jakarta.servlet.http.HttpServletResponse;

public interface CookieUseCase {

    void setToken(HttpServletResponse response, String value, TokenType tokenType);

    void deleteAccessToken(HttpServletResponse response);
}
