package com.somemore.auth.jwt.filter;

import com.somemore.auth.authentication.JwtAuthenticationToken;
import com.somemore.auth.jwt.domain.EncodedToken;
import com.somemore.auth.jwt.domain.TokenType;
import com.somemore.auth.jwt.exception.JwtErrorType;
import com.somemore.auth.jwt.exception.JwtException;
import com.somemore.auth.jwt.usecase.JwtUseCase;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUseCase jwtUseCase;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        EncodedToken accessToken = getAccessToken(request);
        String path = request.getRequestURI();

        return accessToken == null
                || accessToken.isUninitialized()
                || path.equals("/api/center/sign-in");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        EncodedToken accessToken = getAccessToken(request);
        jwtUseCase.processAccessToken(accessToken, response);

        Claims claims = jwtUseCase.getClaims(accessToken);
        Authentication auth = createAuthenticationToken(claims, accessToken);

        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

    private EncodedToken getAccessToken(HttpServletRequest request) {
        EncodedToken accessToken = findAccessTokenFromCookie(request);

        if (accessToken.isUninitialized()) {
            accessToken = findAccessTokenFromHeader(request);
        }

        if (accessToken.isUninitialized()) {
            throw new JwtException(JwtErrorType.MISSING_TOKEN);
        }

        String prefix = "Bearer ";
        return accessToken.removePrefix(prefix);
    }

    private static EncodedToken findAccessTokenFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            return new EncodedToken("UNINITIALIZED");
        }

        return new EncodedToken(authorizationHeader);
    }

    private EncodedToken findAccessTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return new EncodedToken("UNINITIALIZED");
        }

        return Arrays.stream(cookies)
                .filter(Objects::nonNull)
                .filter(cookie -> cookie.getName().equals(TokenType.ACCESS.name()))
                .map(Cookie::getValue)
                .findFirst()
                .map(EncodedToken::new)
                .orElse(new EncodedToken("UNINITIALIZED"));
    }

    private JwtAuthenticationToken createAuthenticationToken(Claims claims,
                                                             EncodedToken accessToken) {
        String userId = claims.get("id", String.class);
        String role = claims.get("role", String.class);

        return new JwtAuthenticationToken(
                userId,
                accessToken,
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}
