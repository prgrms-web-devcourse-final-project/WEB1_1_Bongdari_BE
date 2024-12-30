package com.somemore.global.auth.oauth.repository;

import com.somemore.global.auth.oauth.domain.OAuthInfo;
import com.somemore.global.auth.oauth.domain.OAuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthInfoJpaRepository extends JpaRepository<OAuthInfo, Long> {

    boolean existByOAuthProviderAndOauthId(OAuthProvider provider, String oauthId);
}
