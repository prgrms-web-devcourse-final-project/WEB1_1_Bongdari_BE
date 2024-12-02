package com.somemore.auth.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.somemore.auth.jwt.exception.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            ProblemDetail problemDetail = buildUnauthorizedProblemDetail(e);
            configureUnauthorizedResponse(response);

            objectMapper.writeValue(response.getWriter(), problemDetail);
        }
    }

    private void configureUnauthorizedResponse(HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
    }

    private ProblemDetail buildUnauthorizedProblemDetail(JwtException e) {
        log.error("JwtFilter 예외 발생: {}", e.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "인증에서 오류가 발생했습니다.");
        problemDetail.setTitle("인증 에러");
        problemDetail.setProperty("timestamp", System.currentTimeMillis());
        return problemDetail;
    }
}
