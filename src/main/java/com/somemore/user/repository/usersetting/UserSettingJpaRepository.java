package com.somemore.user.repository.usersetting;

import com.somemore.user.domain.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingJpaRepository extends JpaRepository<UserSetting, Long> {
}
