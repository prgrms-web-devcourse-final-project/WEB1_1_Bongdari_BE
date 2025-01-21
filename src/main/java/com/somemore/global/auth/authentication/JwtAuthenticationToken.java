package com.somemore.global.auth.authentication;

import com.somemore.global.auth.jwt.domain.EncodedToken;
import lombok.EqualsAndHashCode;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final UserIdentity principal;
    private final transient Object credentials;

    public JwtAuthenticationToken(UserIdentity principal,
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

    public static JwtAuthenticationToken of(UserIdentity userIdentity, EncodedToken accessToken) {
        return new JwtAuthenticationToken(
                userIdentity,
                accessToken,
                List.of(new SimpleGrantedAuthority(userIdentity.role().getAuthority()))
        );
    }
}
