package com.somemore.global.auth.oauth.checker;

import com.somemore.global.auth.oauth.domain.OAuthProvider;
import com.somemore.global.auth.oauth.repository.OAuthInfoJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OAuthInfoCheckerImpl implements OAuthInfoChecker {

    private final OAuthInfoJpaRepository oAuthInfoJpaRepository;

    @Override
    public boolean doesUserExist(OAuthProvider provider, String id) {
        return oAuthInfoJpaRepository.existByOAuthProviderAndOauthId(provider, id);
    }
}
