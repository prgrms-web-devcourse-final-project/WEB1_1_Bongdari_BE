package com.somemore.user.repository.usersetting;

import com.somemore.support.IntegrationTestSupport;
import com.somemore.user.domain.UserSetting;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class UserSettingRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private UserSettingRepositoryImpl userSettingRepository;

    @DisplayName("유저 아이디로 유저 세팅을 조회할 수 있다.")
    @Test
    void findByUserId() {
        // given
        UUID userId = UUID.randomUUID();
        UserSetting userSetting = UserSetting.from(userId);
        UserSetting savedUserSetting = userSettingRepository.save(userSetting);

        // when
        Optional<UserSetting> foundUserSetting = userSettingRepository.findByUserId(userId);

        // then
        assertThat(foundUserSetting).isNotNull();
        assertThat(foundUserSetting).get().isEqualTo(savedUserSetting);
    }

    @DisplayName("유저 세팅을 저장할 수 있다.")
    @Test
    void saveUserSetting() {
        // given
        UUID userId = UUID.randomUUID();
        UserSetting userSetting = UserSetting.from(userId);

        // when
        UserSetting savedUserSetting = userSettingRepository.save(userSetting);

        // then
        assertThat(savedUserSetting).isNotNull();
        assertThat(savedUserSetting.getId()).isNotNull();
        assertThat(savedUserSetting.isAuthenticated()).isEqualTo(false);
        assertThat(savedUserSetting.isSmsAgreed()).isEqualTo(false);
    }

}