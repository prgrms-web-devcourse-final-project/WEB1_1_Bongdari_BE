package com.somemore.global.auth.oauth.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.somemore.global.auth.oauth.domain.CommonOAuthInfo;
import com.somemore.global.auth.oauth.domain.OAuthProvider;
import com.somemore.global.auth.oauth.naver.dto.NaverUserProfileResponseDto;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class NaverOAuthResponseConverter implements OAuthResponseConverter {

    @Override
    public CommonOAuthInfo convert(OAuth2User oAuth2User) {
        NaverUserProfileResponseDto naverUserProfileResponseDto = new ObjectMapper().convertValue(oAuth2User.getAttributes(), NaverUserProfileResponseDto.class);

        return new CommonOAuthInfo(
                OAuthProvider.NAVER,
                naverUserProfileResponseDto.response().id());
    }
}
