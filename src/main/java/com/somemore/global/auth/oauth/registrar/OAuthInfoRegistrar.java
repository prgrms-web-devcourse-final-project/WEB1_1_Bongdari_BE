package com.somemore.global.auth.oauth.registrar;

import com.somemore.global.auth.oauth.domain.CommonOAuthInfo;
import com.somemore.global.auth.oauth.domain.OAuthInfo;
import com.somemore.user.domain.User;

public interface OAuthInfoRegistrar {
    OAuthInfo register(User user, CommonOAuthInfo oauthInfo);
}
