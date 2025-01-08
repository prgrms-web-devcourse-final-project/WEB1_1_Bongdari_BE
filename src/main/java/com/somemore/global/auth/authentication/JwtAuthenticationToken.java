package com.somemore.global.auth.authentication;

import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.user.domain.User;
import lombok.EqualsAndHashCode;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final Serializable principal;
    private final transient Object credentials;

    public JwtAuthenticationToken(Serializable principal,
                                  Object credentials,
                                  Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public static JwtAuthenticationToken of(User user, EncodedToken accessToken) {
        return new JwtAuthenticationToken(
                user.getId(),
                accessToken,
                List.of(new SimpleGrantedAuthority(user.getRole().getAuthority()))
        );
    }

    public static JwtAuthenticationToken of(String userId, String role, EncodedToken accessToken) {
        return new JwtAuthenticationToken(
                userId,
                accessToken,
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}
