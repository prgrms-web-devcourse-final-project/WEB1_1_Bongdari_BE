package com.somemore.global.auth.oauth.naver.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.somemore.global.auth.oauth.naver.dto.response.NaverUserProfileResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuthResponseConverter {

    public static NaverUserProfileResponseDto convertToNaverUserProfileResponseDto(OAuth2User oAuth2User) {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        return objectMapper.convertValue(attributes, NaverUserProfileResponseDto.class);
    }
}
