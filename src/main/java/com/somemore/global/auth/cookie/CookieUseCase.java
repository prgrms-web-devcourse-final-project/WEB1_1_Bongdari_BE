package com.somemore.global.auth.cookie;

import jakarta.servlet.http.HttpServletResponse;

public interface CookieUseCase {
    void setLoginToken(HttpServletResponse response, String value);

    void setAccessToken(HttpServletResponse response, String value);

    void deleteAccessToken(HttpServletResponse response);
}
