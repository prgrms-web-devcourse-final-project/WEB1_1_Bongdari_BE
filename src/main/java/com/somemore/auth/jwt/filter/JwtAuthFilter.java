package com.somemore.auth.jwt.filter;

import com.somemore.auth.jwt.domain.EncodedToken;
import com.somemore.auth.jwt.exception.JwtErrorType;
import com.somemore.auth.jwt.exception.JwtException;
import com.somemore.auth.jwt.usecase.JwtUseCase;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUseCase jwtUseCase;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return token == null || token.isEmpty();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        EncodedToken accessToken = getAccessToken(request);
        jwtUseCase.processAccessToken(accessToken, response);

        Claims claims = jwtUseCase.getClaims(accessToken);
        Authentication auth = createAuthenticationToken(claims, accessToken);

        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

    private EncodedToken getAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if (!accessToken.startsWith("Bearer ")) {
            throw new JwtException(JwtErrorType.MISSING_TOKEN);
        }

        accessToken = accessToken.substring(7);

        return new EncodedToken(accessToken);
    }

    private JwtAuthenticationToken createAuthenticationToken(Claims claims,
            EncodedToken accessToken) {
        String userId = claims.get("id", String.class);
        UserRole role = UserRole.valueOf(claims.get("role", String.class));

        return new JwtAuthenticationToken(
                userId,
                accessToken,
                List.of(new SimpleGrantedAuthority(role.name()))
        );
    }
}
