package com.somemore.auth.redirect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface RedirectUseCase {
    void redirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException;
}