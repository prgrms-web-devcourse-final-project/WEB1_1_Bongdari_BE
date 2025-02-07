package com.somemore.user.usecase;

import com.somemore.global.auth.oauth.domain.CommonOAuthInfo;
import com.somemore.user.domain.User;
import com.somemore.user.domain.UserAuthInfo;
import com.somemore.user.domain.UserRole;

public interface RegisterUserUseCase {
    User registerOAuthUser(CommonOAuthInfo commonOAuthInfo, UserRole role);

    User registerLocalUser(UserAuthInfo userAuthInfo, UserRole role);
}
