package com.somemore.global.auth.idpw.provider;

import com.somemore.center.usecase.NEWCenterQueryUseCase;
import com.somemore.global.auth.authentication.JwtAuthenticationToken;
import com.somemore.global.auth.authentication.UserIdentity;
import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.domain.TokenType;
import com.somemore.global.auth.jwt.usecase.JwtUseCase;
import com.somemore.user.domain.User;
import com.somemore.user.domain.UserRole;
import com.somemore.user.usecase.UserQueryUseCase;
import com.somemore.volunteer.usecase.NEWVolunteerQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final JwtUseCase jwtUseCase;
    private final PasswordEncoder passwordEncoder;
    private final UserQueryUseCase userQueryUseCase;
    private final NEWVolunteerQueryUseCase volunteerQueryUseCase;
    private final NEWCenterQueryUseCase centerQueryUseCase;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String accountId = authentication.getName();
        String rawAccountPassword = authentication.getCredentials().toString();

        User user = userQueryUseCase.getByAccountId(accountId);

        validatePassword(rawAccountPassword, user.getAccountPassword());

        UUID userId = user.getId();
        UserRole role = user.getRole();
        UUID roleId = getRoleIdByUserId(role, user.getId());

        UserIdentity userIdentity = UserIdentity.of(userId, roleId, role);

        EncodedToken accessToken = generateAccessToken(userIdentity);

        return JwtAuthenticationToken.of(userIdentity, accessToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private UUID getRoleIdByUserId(UserRole role, UUID userId) {
        if (role.equals(UserRole.VOLUNTEER)) {
            return volunteerQueryUseCase.getIdByUserId(userId);
        }
        return centerQueryUseCase.getIdByUserId(userId);
    }

    private EncodedToken generateAccessToken(UserIdentity userIdentity) {
        return jwtUseCase.generateToken(
                userIdentity,
                TokenType.ACCESS
        );
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        // TODO 비활성 계정 검증
    }
}
