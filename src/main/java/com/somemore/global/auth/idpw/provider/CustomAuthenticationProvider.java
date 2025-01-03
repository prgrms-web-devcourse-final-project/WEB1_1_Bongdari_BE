package com.somemore.global.auth.idpw.provider;

import com.somemore.global.auth.authentication.JwtAuthenticationToken;
import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.domain.TokenType;
import com.somemore.user.domain.UserRole;
import com.somemore.global.auth.jwt.usecase.JwtUseCase;
import com.somemore.domains.center.usecase.query.CenterSignUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CenterSignUseCase centerSignUseCase;
    private final JwtUseCase jwtUseCase;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String accountId = authentication.getName();
        String rawAccountPassword = authentication.getCredentials().toString();

        String centerId = centerSignUseCase.getIdByAccountId(accountId).toString();
        String encodedPassword = centerSignUseCase.getPasswordByAccountId(accountId);

        if (passwordEncoder.matches(rawAccountPassword, encodedPassword)) {
            EncodedToken accessToken = jwtUseCase.generateToken(
                    centerId,
                    UserRole.CENTER.getAuthority(),
                    TokenType.ACCESS
            );

            return new JwtAuthenticationToken(
                    centerId,
                    accessToken,
                    List.of(new SimpleGrantedAuthority(UserRole.CENTER.getAuthority()))
            );
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
