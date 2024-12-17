package com.somemore.global.auth.redirect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedirectService implements RedirectUseCase {

    private final RedirectStrategy redirectStrategy;

    @Override
    public void redirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        redirectStrategy.sendRedirect(request, response, url);
    }
}
