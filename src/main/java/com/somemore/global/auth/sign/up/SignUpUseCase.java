package com.somemore.global.auth.sign.up;

import com.somemore.global.auth.oauth.domain.CommonOAuthInfo;

public interface SignUpUseCase {

    void signUpLocalUser(SignUpRequestDto signUpRequestDto);

    void signUpOAuthUser(CommonOAuthInfo oAuthInfo);
}
