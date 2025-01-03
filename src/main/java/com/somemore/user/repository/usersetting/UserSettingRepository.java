package com.somemore.user.repository.usersetting;

import com.somemore.user.domain.UserSetting;

import java.util.Optional;
import java.util.UUID;

public interface UserSettingRepository {

    Optional<UserSetting> findByUserId(UUID userId);
    UserSetting save(UserSetting userSetting);
}
