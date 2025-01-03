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
        User user = User.from(userAuthInfo, UserRole.VOLUNTEER);

        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getRole()).isEqualTo(UserRole.VOLUNTEER);
        assertThat(savedUser.getEmail()).isEqualTo(userAuthInfo.email());
    }

    @DisplayName("OAuth 유저(기관)를 저장할 수 있다.")
    @Test
    void saveOAuthCenterUser() {
        // given
        UserAuthInfo userAuthInfo = UserAuthInfo.createForOAuth(OAuthProvider.NAVER);
        User user = User.from(userAuthInfo, UserRole.CENTER);

        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getRole()).isEqualTo(UserRole.CENTER);
        assertThat(savedUser.getEmail()).isEqualTo(userAuthInfo.email());
    }

    @DisplayName("Local 유저(기관)를 저장할 수 있다.")
    @Test
    void saveLocalCenterUser() {
        // given
        UserAuthInfo userAuthInfo = new UserAuthInfo("test@test.test", "test");
        User user = User.from(userAuthInfo, UserRole.CENTER);

        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getRole()).isEqualTo(UserRole.CENTER);
        assertThat(savedUser.getEmail()).isEqualTo(userAuthInfo.email());
    }

    @DisplayName("유저아이디로 유저를 조회할 수 있다.")
    @Test
    void findById() {
        // given
        UserAuthInfo userAuthInfo = new UserAuthInfo("test@test.test", "test");
        User user = User.from(userAuthInfo, UserRole.CENTER);
        User savedUser = userRepository.save(user);

        // when
        Optional<User> findUser = userRepository.findById(savedUser.getId());

        // then
        assertThat(findUser).isPresent();
        assertThat(findUser.get().getId()).isEqualTo(savedUser.getId());
        assertThat(findUser.get().getRole()).isEqualTo(UserRole.CENTER);
        assertThat(savedUser.getEmail()).isEqualTo(userAuthInfo.email());
    }

    @DisplayName("유효하지 않은 유저 아이디로 유저를 조회할 수 없다.")
    @Test
    void findByInvalidId() {
        // given
        UserAuthInfo userAuthInfo = new UserAuthInfo("test@test.test", "test");
        User user = User.from(userAuthInfo, UserRole.CENTER);
        userRepository.save(user);
        UUID invalidUserId = UUID.randomUUID();

        // when
        Optional<User> findUser = userRepository.findById(invalidUserId);

        // then
        assertThat(findUser).isEmpty();
    }
}
