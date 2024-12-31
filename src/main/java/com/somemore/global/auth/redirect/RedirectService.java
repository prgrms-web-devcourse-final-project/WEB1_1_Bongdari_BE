package com.somemore.global.auth.redirect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedirectService implements RedirectUseCase {

    @Value("${app.front-url}")
    private String frontUrl;

    private final RedirectStrategy redirectStrategy;

    private static final String ERROR_PATH = "/error";

    @Override
    public void redirect(HttpServletRequest request, HttpServletResponse response, String path) {
        try {
            redirectStrategy.sendRedirect(request, response, path);
        } catch (IOException e) {
            log.error("리디렉션 중 오류 발생 - 대상 URL: {}, 메시지: {}", path, e.getMessage());
            handleRedirectFailure(request, response);
        }
    }

    private void handleRedirectFailure(HttpServletRequest request, HttpServletResponse response) {
        String fallbackUrl = buildFallbackUrl();
        try {
            redirectStrategy.sendRedirect(request, response, fallbackUrl);
        } catch (IOException e) {
            log.error("에러 페이지로 리디렉션 시도 중 오류 발생 - 메시지: {}", e.getMessage());
        }
    }

    private String buildFallbackUrl() {
        return frontUrl + RedirectService.ERROR_PATH;
    }
}