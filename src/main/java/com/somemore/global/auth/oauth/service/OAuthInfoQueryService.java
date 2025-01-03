package com.somemore.global.auth.oauth.service;

import com.somemore.global.auth.oauth.domain.CommonOAuthInfo;
import com.somemore.global.auth.oauth.repository.OAuthInfoRepository;
import com.somemore.global.auth.oauth.usecase.OAuthInfoQueryUseCase;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OAuthInfoQueryService implements OAuthInfoQueryUseCase {

    private final OAuthInfoRepository oAuthInfoRepository;

    public UUID getUserIdByCommonOAuthInfo(CommonOAuthInfo commonOAuthInfo) {
        return oAuthInfoRepository.findUserIdByOAuthProviderAndOauthId(
                commonOAuthInfo.provider(),
                commonOAuthInfo.oauthId()
        ).orElseThrow(EntityNotFoundException::new);
    }
}
