package com.somemore.user.usecase;

import com.somemore.user.domain.UserRole;
import com.somemore.global.auth.oauth.domain.CommonOAuthInfo;
import com.somemore.user.domain.User;
import com.somemore.user.dto.UserAuthInfoRequestDto;

public interface RegisterUserUseCase {
    User registerOAuthUser(CommonOAuthInfo commonOAuthInfo, UserRole role);
    User registerLocalUser(UserAuthInfoRequestDto userAuthInfoRequestDto, UserRole role);
}
