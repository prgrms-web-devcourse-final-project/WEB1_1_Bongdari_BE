package com.somemore.global.auth.oauth.handler;

import com.somemore.global.auth.redirect.RedirectUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final RedirectUseCase redirectUseCase;

    private static final String FAILURE_PATH = "/error/login";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        redirectUseCase.redirect(request, response, FAILURE_PATH);
        log.error("OAuth 로그인 실패: {}", exception.getMessage());
    }
}
