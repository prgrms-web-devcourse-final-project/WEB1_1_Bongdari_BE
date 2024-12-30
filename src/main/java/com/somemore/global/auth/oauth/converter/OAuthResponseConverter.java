package com.somemore.global.auth.oauth.converter;

import com.somemore.global.auth.oauth.domain.CommonOAuthInfo;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuthResponseConverter {
    CommonOAuthInfo convert(OAuth2User oAuth2User);
}
