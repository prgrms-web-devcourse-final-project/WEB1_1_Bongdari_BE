package com.somemore.global.auth.oauth.usecase;

import com.somemore.global.auth.oauth.domain.CommonOAuthInfo;

import java.util.UUID;

public interface OAuthInfoQueryUseCase {

    UUID getUserIdByCommonOAuthInfo(CommonOAuthInfo commonOAuthInfo);
}
