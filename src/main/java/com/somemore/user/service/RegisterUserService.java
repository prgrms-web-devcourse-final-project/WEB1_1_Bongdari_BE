package com.somemore.user.service;

import com.somemore.global.auth.oauth.domain.CommonOAuthInfo;
import com.somemore.user.domain.User;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.domain.UserRole;
import com.somemore.user.domain.UserSetting;
import com.somemore.user.dto.UserAuthInfo;
import com.somemore.user.repository.user.UserRepository;
import com.somemore.user.repository.usercommonattribute.UserCommonAttributeRepository;
import com.somemore.user.repository.usersetting.UserSettingRepository;
import com.somemore.user.usecase.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final UserSettingRepository userSettingRepository;
    private final UserCommonAttributeRepository userCommonAttributeRepository;

    @Override
    public User registerOAuthUser(CommonOAuthInfo commonOAuthInfo, UserRole role) {
        UserAuthInfo userAuthInfo = UserAuthInfo.createForOAuth(commonOAuthInfo.provider());
        return createAndRegisterUser(role, userAuthInfo);
    }

    @Override
    public User registerLocalUser(UserAuthInfo userAuthInfo, UserRole role) {
        return createAndRegisterUser(role, userAuthInfo);
    }

    private User createAndRegisterUser(UserRole role, UserAuthInfo userAuthInfo) {
        User user = User.of(userAuthInfo, role);
        userRepository.save(user);
        UUID userId = user.getId();

        UserSetting userSetting = UserSetting.from(userId);
        userSettingRepository.save(userSetting);

        UserCommonAttribute userCommonAttribute = UserCommonAttribute.createDefault(userId);
        userCommonAttributeRepository.save(userCommonAttribute);

        return user;
    }
}
