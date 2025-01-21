package com.somemore.support.annotation;

import com.somemore.global.auth.authentication.JwtAuthenticationToken;
import com.somemore.global.auth.authentication.UserIdentity;
import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.user.domain.UserRole;
import io.jsonwebtoken.Claims;
import java.util.Collections;
import java.util.UUID;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class MockUserSecurityContextFactory implements WithSecurityContextFactory<MockUser> {

    @Override
    public SecurityContext createSecurityContext(MockUser user) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        JwtAuthenticationToken auth = createAuthenticationToken(user);
        context.setAuthentication(auth);
        return context;
    }

    private JwtAuthenticationToken createAuthenticationToken(MockUser user) {
        UUID userId = UUID.fromString(user.userId());
        UUID roleId = UUID.fromString(user.roleId());
        UserRole userRole = UserRole.from(user.role());
        UserIdentity userIdentity = UserIdentity.of(userId, roleId, userRole);

        return JwtAuthenticationToken.of(userIdentity, new EncodedToken(""));
    }
}
