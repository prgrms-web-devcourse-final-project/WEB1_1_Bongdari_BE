package com.somemore.global.auth.oauth.service;

import com.somemore.global.auth.oauth.OAuthProvider;
import com.somemore.global.auth.oauth.naver.service.command.NaverOAuth2UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final NaverOAuth2UserInfoService naverOAuth2UserInfoService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
            OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

            return switch (OAuthProvider.from(getRegistrationId(userRequest))) {
                case NAVER -> naverOAuth2UserInfoService.processOAuth2User(oAuth2User);
            };
        } catch (OAuth2AuthenticationException e) {
            log.error("OAuth 사용자 정보를 로드하는 중 문제가 발생했습니다: {}", e.getMessage(), e);
            throw e;
        }
    }

    private static String getRegistrationId(OAuth2UserRequest userRequest) {
        return userRequest.getClientRegistration().getRegistrationId();
    }
}
