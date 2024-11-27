package com.somemore.auth.cookie;

import jakarta.servlet.http.HttpServletResponse;

public interface CookieUseCase {
    void setAccessToken(HttpServletResponse response, String value);

    void deleteAccessToken(HttpServletResponse response);
}
