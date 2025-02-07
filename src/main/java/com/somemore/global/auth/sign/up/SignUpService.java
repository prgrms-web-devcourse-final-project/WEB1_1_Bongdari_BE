package com.somemore.global.auth.sign.up;

import com.somemore.center.usecase.NEWRegisterCenterUseCase;
import com.somemore.global.auth.oauth.domain.CommonOAuthInfo;
import com.somemore.global.auth.oauth.registrar.OAuthInfoRegistrar;
import com.somemore.user.domain.User;
import com.somemore.user.domain.UserAuthInfo;
import com.somemore.user.domain.UserRole;
import com.somemore.user.service.RegisterUserService;
import com.somemore.volunteer.usecase.NEWRegisterVolunteerUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SignUpService implements SignUpUseCase {

    private final RegisterUserService registerUserUseCase;
    private final OAuthInfoRegistrar oauthInfoRegistrar;
    private final NEWRegisterVolunteerUseCase NEWRegisterVolunteerUseCase;
    private final NEWRegisterCenterUseCase NEWRegisterCenterUseCase;

    @Override
    public void signUpLocalUser(SignUpRequestDto signUpRequestDto) {
        User user = registerUserUseCase.registerLocalUser(
                UserAuthInfo.of(signUpRequestDto.accountId(), signUpRequestDto.accountPassword()),
                UserRole.from(signUpRequestDto.userRole()));

        // TODO 회원가입 이벤트 발행으로 변경 (봉사자 혹은 기관 등록)
        registerVolunteerOrCenter(user);
    }

    @Override
    public void signUpOAuthUser(CommonOAuthInfo oAuthInfo) {
        User user = registerUserUseCase.registerOAuthUser(
                oAuthInfo, UserRole.getOAuthUserDefaultRole());

        oauthInfoRegistrar.register(user, oAuthInfo);
        registerVolunteerOrCenter(user);
    }

    private void registerVolunteerOrCenter(User user) {
        UUID userId = user.getId();
        UserRole role = user.getRole();

        if (role == UserRole.VOLUNTEER) {
            NEWRegisterVolunteerUseCase.register(userId);
            return;
        }

        NEWRegisterCenterUseCase.register(userId);
    }
}
