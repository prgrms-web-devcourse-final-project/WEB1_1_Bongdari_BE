package com.somemore.global.auth.redirect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface RedirectUseCase {
    void redirect(HttpServletRequest request, HttpServletResponse response, String path);
}
