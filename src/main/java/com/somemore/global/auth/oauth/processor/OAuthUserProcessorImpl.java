package com.somemore.global.auth.oauth.processor;

import com.somemore.global.auth.oauth.checker.OAuthInfoChecker;
import com.somemore.global.auth.oauth.converter.OAuthResponseConverter;
import com.somemore.global.auth.oauth.domain.CommonOAuthInfo;
import com.somemore.global.auth.oauth.domain.CustomOAuth2User;
import com.somemore.global.auth.oauth.service.OAuthInfoQueryService;
import com.somemore.global.auth.sign.up.SignUpUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthUserProcessorImpl implements OAuthUserProcessor {

    private final OAuthResponseConverter oauthResponseConverter;
    private final OAuthInfoChecker oauthInfoChecker;
    private final OAuthInfoQueryService oAuthInfoQueryService;
    private final SignUpUseCase signUpUseCase;

    @Override
    public UUID fetchUserIdByOAuthUser(CustomOAuth2User oauthUser) {
        CommonOAuthInfo oauthInfo = oauthResponseConverter.convert(oauthUser);
        return findUserIdByOAuthInfo(oauthInfo);
    }

    private UUID findUserIdByOAuthInfo(CommonOAuthInfo oauthInfo) {
        if (isNewUser(oauthInfo)) {
            signUpUseCase.signUpOAuthUser(oauthInfo);
        }
        return oAuthInfoQueryService.getUserIdByCommonOAuthInfo(oauthInfo);
    }

    private boolean isNewUser(CommonOAuthInfo oauthInfo) {
        return !oauthInfoChecker.doesUserExist(oauthInfo.provider(), oauthInfo.oauthId());
    }
}
