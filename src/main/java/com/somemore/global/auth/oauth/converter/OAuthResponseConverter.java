package com.somemore.global.auth.oauth.converter;

import com.somemore.global.auth.oauth.domain.CommonOAuthInfo;
import com.somemore.global.auth.oauth.domain.CustomOAuth2User;

public interface OAuthResponseConverter {
    CommonOAuthInfo convert(CustomOAuth2User oAuth2User);
}
