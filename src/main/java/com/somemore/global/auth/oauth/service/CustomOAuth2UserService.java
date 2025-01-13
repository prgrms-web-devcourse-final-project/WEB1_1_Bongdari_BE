package com.somemore.global.auth.oauth.service;

import com.somemore.global.auth.oauth.domain.CustomOAuth2User;
import com.somemore.global.auth.oauth.domain.OAuthProvider;
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

    private final DefaultOAuth2UserService oauth2UserService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            OAuth2User oAuth2User = extractOAuth2User(userRequest);
            OAuthProvider oAuthProvider = extractOAuthProvider(userRequest);

            return CustomOAuth2User.of(oAuth2User, oAuthProvider);
        } catch (OAuth2AuthenticationException e) {
            log.error("OAuth 사용자 정보를 불러오는 중 문제가 발생했습니다: {}", e.getMessage(), e);
            throw e;
        }
    }

    private OAuth2User extractOAuth2User(OAuth2UserRequest userRequest) {
        return oauth2UserService.loadUser(userRequest);
    }

    private OAuthProvider extractOAuthProvider(OAuth2UserRequest userRequest) {
        return OAuthProvider.from(userRequest.getClientRegistration().getRegistrationId());
    }
}
