package com.somemore.user.service;

import com.somemore.global.auth.oauth.domain.OAuthProvider;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.user.domain.User;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.domain.UserRole;
import com.somemore.user.dto.UserAuthInfo;
import com.somemore.user.repository.user.UserRepository;
import com.somemore.user.repository.usercommonattribute.UserCommonAttributeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class UserQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserQueryService userQueryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCommonAttributeRepository userCommonAttributeRepository;

    private User user;

    @BeforeEach
    void setup() {
        UserAuthInfo userAuthInfo = UserAuthInfo.createForOAuth(OAuthProvider.NAVER);

        user = userRepository.save(User.from(userAuthInfo, UserRole.VOLUNTEER));
        userCommonAttributeRepository.save(UserCommonAttribute.createDefault(user.getId()));
    }


    @DisplayName("유저 아이디로 유저를 조회할 수 있다.")
    @Test
    void getById() {
        // given

        // when
        User foundUser = userQueryService.getById(user.getId());

        // then
        assertThat(foundUser).isNotNull();
    }

    @DisplayName("유저 계정 아이디로 유저를 조회할 수 있다.")
    @Test
    void getByAccountId() {
        // given

        // when
        User foundUser = userQueryService.getByAccountId(user.getAccountId());

        // then
        assertThat(foundUser).isNotNull();
    }

    @DisplayName("유저 아이디로 유저 공통 속성을 조회할 수 있다.")
    @Test
    void getCommonAttributeByUserID() {
        // given

        // when
        UserCommonAttribute foundCommonAttribute = userQueryService.getCommonAttributeByUserId(user.getId());

        // then
        assertThat(foundCommonAttribute).isNotNull();
    }

    @DisplayName("유저가 필수 입력 필드를 사용자화하지 않은 경우 기본 값 false를 반환한다.")
    @Test
    void getIsCustomizedByUserId_ReturnsFalse_WhenDefaultValue() {
        // given

        // when
        boolean isCustomized = userQueryService.getIsCustomizedByUserId(user.getId());

        // then
        assertThat(isCustomized).isFalse();
    }
}
