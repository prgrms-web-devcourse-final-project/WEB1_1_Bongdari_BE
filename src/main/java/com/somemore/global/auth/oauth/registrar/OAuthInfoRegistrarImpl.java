package com.somemore.global.auth.oauth.registrar;

import com.somemore.global.auth.oauth.domain.CommonOAuthInfo;
import com.somemore.global.auth.oauth.domain.OAuthInfo;
import com.somemore.global.auth.oauth.repository.OAuthInfoRepository;
import com.somemore.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OAuthInfoRegistrarImpl implements OAuthInfoRegistrar {

    private final OAuthInfoRepository oauthInfoRepository;

    @Override
    public OAuthInfo register(User user, CommonOAuthInfo commonOAuthInfo) {
        return oauthInfoRepository.save(
                createOAuthInfo(user, commonOAuthInfo));
    }

    private OAuthInfo createOAuthInfo(User user, CommonOAuthInfo commonOAuthInfo) {
        return OAuthInfo.builder()
                .userId(user.getId())
                .oauthId(commonOAuthInfo.oauthId())
                .oAuthProvider(commonOAuthInfo.provider())
                .build();
    }
}