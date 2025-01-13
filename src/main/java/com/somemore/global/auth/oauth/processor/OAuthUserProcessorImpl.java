package com.somemore.global.auth.oauth.processor;

import com.somemore.global.auth.oauth.checker.OAuthInfoChecker;
import com.somemore.global.auth.oauth.converter.OAuthResponseConverter;
import com.somemore.global.auth.oauth.domain.CommonOAuthInfo;
import com.somemore.global.auth.oauth.domain.CustomOAuth2User;
import com.somemore.global.auth.oauth.registrar.OAuthInfoRegistrar;
import com.somemore.global.auth.oauth.service.OAuthInfoQueryService;
import com.somemore.user.domain.User;
import com.somemore.user.domain.UserRole;
import com.somemore.user.usecase.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OAuthUserProcessorImpl implements OAuthUserProcessor {

    private final OAuthResponseConverter oauthResponseConverter;
    private final OAuthInfoChecker oauthInfoChecker;
    private final OAuthInfoRegistrar oauthInfoRegistrar;
    private final RegisterUserUseCase registerUserUseCase;
    private final OAuthInfoQueryService oAuthInfoQueryService;

    @Override
    public UUID fetchUserIdByOAuthUser(CustomOAuth2User oauthUser) {
        CommonOAuthInfo oauthInfo = oauthResponseConverter.convert(oauthUser);
        return findUserIdByOAuthInfo(oauthInfo);
    }

    private UUID findUserIdByOAuthInfo(CommonOAuthInfo oauthInfo) {
        if (isNewUser(oauthInfo)) {
            User user = registerOAuthUser(oauthInfo);
            return user.getId();
        }
        return oAuthInfoQueryService.getUserIdByCommonOAuthInfo(oauthInfo);
    }

    private User registerOAuthUser(CommonOAuthInfo oauthInfo) {
        User user = registerUser(oauthInfo);
        registerOAuthInfo(user, oauthInfo);
        // TODO 봉사자 등록 이벤트 발행

        return user;
    }

    private boolean isNewUser(CommonOAuthInfo oauthInfo) {
        return !oauthInfoChecker.doesUserExist(oauthInfo.provider(), oauthInfo.oauthId());
    }

    private User registerUser(CommonOAuthInfo oauthInfo) {
        return registerUserUseCase.registerOAuthUser(oauthInfo, UserRole.getOAuthUserDefaultRole());
    }

    private void registerOAuthInfo(User user, CommonOAuthInfo oauthInfo) {
        oauthInfoRegistrar.register(user, oauthInfo);
    }
}
