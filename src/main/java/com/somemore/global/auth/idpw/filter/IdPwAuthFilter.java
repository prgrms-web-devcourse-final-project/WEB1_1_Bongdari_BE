package com.somemore.global.auth.idpw.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.somemore.global.auth.authentication.UserIdentity;
import com.somemore.global.auth.cookie.CookieUseCase;
import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.domain.TokenType;
import com.somemore.global.auth.jwt.usecase.GenerateTokensOnLoginUseCase;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class IdPwAuthFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final GenerateTokensOnLoginUseCase generateTokensOnLoginUseCase;
    private final CookieUseCase cookieUseCase;
    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String accountId = request.getParameter("account_id");
        String accountPassword = request.getParameter("account_password");

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(accountId, accountPassword);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        response.setStatus(HttpServletResponse.SC_OK);

        UserIdentity userIdentity = (UserIdentity) authResult.getPrincipal();

        processToken(response, userIdentity);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        ProblemDetail problemDetail = buildUnauthorizedProblemDetail(failed);
        configureUnauthorizedResponse(response);

        objectMapper.writeValue(response.getWriter(), problemDetail);
    }

    private void processToken(HttpServletResponse response, UserIdentity userIdentity) {
        generateTokensOnLoginUseCase.generateAuthTokensAndReturnAccessToken(userIdentity);

        EncodedToken loginToken = generateTokensOnLoginUseCase.generateLoginToken(userIdentity);

        cookieUseCase.setToken(response, loginToken.value(), TokenType.SIGN_IN);
    }

    private void configureUnauthorizedResponse(HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
    }

    private ProblemDetail buildUnauthorizedProblemDetail(AuthenticationException e) {
        log.error("IdPwAuthFilter 예외 발생: {}", e.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "ID, PW 인증/인가에서 오류가 발생했습니다.");
        problemDetail.setTitle("인증/인가 에러");
        problemDetail.setProperty("timestamp", System.currentTimeMillis());
        return problemDetail;
    }
}
