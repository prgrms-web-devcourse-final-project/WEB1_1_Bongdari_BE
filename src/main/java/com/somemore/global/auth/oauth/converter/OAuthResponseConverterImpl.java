package com.somemore.global.auth.oauth.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.somemore.global.auth.oauth.domain.CommonOAuthInfo;
import com.somemore.global.auth.oauth.domain.CustomOAuth2User;
import com.somemore.global.auth.oauth.domain.OAuthProvider;
import com.somemore.global.auth.oauth.dto.KakaoUserProfileResponseDto;
import com.somemore.global.auth.oauth.dto.NaverUserProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuthResponseConverterImpl implements OAuthResponseConverter {

    private final ObjectMapper objectMapper;

    @Override
    public CommonOAuthInfo convert(CustomOAuth2User oAuth2User) {
        OAuthProvider provider = oAuth2User.getProvider();
        switch (provider) {
            case KAKAO:
                KakaoUserProfileResponseDto kakaoUserProfile = objectMapper.convertValue(
                        oAuth2User.getAttributes(),
                        KakaoUserProfileResponseDto.class
                );
                return  CommonOAuthInfo.of(
                        OAuthProvider.KAKAO,
                        kakaoUserProfile.id()
                );
            case NAVER:
                NaverUserProfileResponseDto naverUserProfile = objectMapper.convertValue(
                        oAuth2User.getAttributes(),
                        NaverUserProfileResponseDto.class
                );
                return CommonOAuthInfo.of(
                        OAuthProvider.NAVER,
                        naverUserProfile.response().id()
                );
            default:
                throw new IllegalArgumentException("지원하지 않는 OAuth Provider입니다.");
        }
    }
}
