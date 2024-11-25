package com.somemore.auth.oauth.naver.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.somemore.auth.oauth.naver.dto.response.NaverUserProfileResponseDto;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class OAuthResponseConverter {

    private OAuthResponseConverter(){
    }

    public static NaverUserProfileResponseDto convertToNaverUserProfileResponseDto(OAuth2User oAuth2User) {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        return objectMapper.convertValue(attributes, NaverUserProfileResponseDto.class);
    }
}
