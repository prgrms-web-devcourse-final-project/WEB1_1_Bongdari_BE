package com.somemore.auth.cookie;

import com.somemore.auth.jwt.domain.TokenType;
import jakarta.servlet.http.HttpServletResponse;

public interface SetCookieUseCase {
    void setToken(HttpServletResponse response, String value, TokenType tokenType);
}
