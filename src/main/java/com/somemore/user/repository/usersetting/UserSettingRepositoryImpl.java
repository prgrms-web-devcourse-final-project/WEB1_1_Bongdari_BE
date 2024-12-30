package com.somemore.user.repository.usersetting;

import com.somemore.user.domain.UserSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserSettingRepositoryImpl implements UserSettingRepository {

    private final UserSettingJpaRepository userSettingJpaRepository;

    @Override
    public UserSetting save(UserSetting userSetting) {
        return userSettingJpaRepository.save(userSetting);
    }
}
