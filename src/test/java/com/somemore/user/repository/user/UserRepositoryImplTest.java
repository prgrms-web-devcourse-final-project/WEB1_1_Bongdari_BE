package com.somemore.user.repository.user;

import com.somemore.global.auth.oauth.domain.OAuthProvider;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.user.domain.User;
import com.somemore.user.domain.UserRole;
import com.somemore.user.dto.UserAuthInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class UserRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private UserRepositoryImpl userRepository;

    @DisplayName("OAuth 유저(봉사자)를 저장할 수 있다.")
    @Test
    void saveOAuthVolunteerUser() {
        // given
        UserAuthInfo userAuthInfo = UserAuthInfo.createForOAuth(OAuthProvider.NAVER);
        User user = User.of(userAuthInfo, UserRole.VOLUNTEER);

        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser)
                .isNotNull()
                .isEqualTo(user);
    }

    @DisplayName("OAuth 유저(기관)를 저장할 수 있다.")
    @Test
    void saveOAuthCenterUser() {
        // given
        UserAuthInfo userAuthInfo = UserAuthInfo.createForOAuth(OAuthProvider.NAVER);
        User user = User.of(userAuthInfo, UserRole.CENTER);

        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser)
                .isNotNull()
                .isEqualTo(user);
    }

    @DisplayName("Local 유저(기관)를 저장할 수 있다.")
    @Test
    void saveLocalCenterUser() {
        // given
        UserAuthInfo userAuthInfo = new UserAuthInfo("test@test.test", "test");
        User user = User.of(userAuthInfo, UserRole.CENTER);

        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser)
                .isNotNull()
                .isEqualTo(user);
    }

    @DisplayName("유저아이디로 유저를 조회할 수 있다.")
    @Test
    void findById() {
        // given
        UserAuthInfo userAuthInfo = new UserAuthInfo("test@test.test", "test");
        User user = User.of(userAuthInfo, UserRole.CENTER);
        User savedUser = userRepository.save(user);

        // when
        Optional<User> findUser = userRepository.findById(savedUser.getId());

        // then
        assertThat(findUser).isPresent();
        assertThat(findUser.get()).isEqualTo(savedUser);
    }

    @DisplayName("유효하지 않은 유저 아이디로 유저를 조회할 수 없다.")
    @Test
    void findByInvalidId() {
        // given
        UserAuthInfo userAuthInfo = new UserAuthInfo("test@test.test", "test");
        User user = User.of(userAuthInfo, UserRole.CENTER);
        userRepository.save(user);
        UUID invalidUserId = UUID.randomUUID();

        // when
        Optional<User> findUser = userRepository.findById(invalidUserId);

        // then
        assertThat(findUser).isEmpty();
    }

    @DisplayName("유저 아이디로 유저 권한을 조회할 수 있다.")
    @Test
    void findRoleById() {
        // given
        UserAuthInfo userAuthInfo = new UserAuthInfo("test@test.test", "test");
        User user = User.of(userAuthInfo, UserRole.CENTER);
        User savedUser = userRepository.save(user);

        // when
        Optional<UserRole> role = userRepository.findRoleById(savedUser.getId());

        // then
        assertThat(role).isPresent();
        assertThat(role.get()).isEqualTo(savedUser.getRole());
    }

    @DisplayName("유저 계정 아이디로 유저를 조회할 수 있다.")
    @Test
    void findByAccountId() {
        // given
        UserAuthInfo userAuthInfo = new UserAuthInfo("test@test.test", "test");
        User user = User.of(userAuthInfo, UserRole.CENTER);
        User savedUser = userRepository.save(user);

        // when
        Optional<User> findUser = userRepository.findByAccountId(savedUser.getAccountId());

        // then
        assertThat(findUser).isPresent();
        assertThat(findUser.get()).isEqualTo(savedUser);
    }
}
