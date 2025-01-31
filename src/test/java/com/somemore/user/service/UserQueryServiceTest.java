package com.somemore.user.service;

import com.somemore.global.auth.oauth.domain.OAuthProvider;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.user.domain.User;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.domain.UserRole;
import com.somemore.user.dto.UserAuthInfo;
import com.somemore.user.repository.user.UserRepository;
import com.somemore.user.repository.usercommonattribute.UserCommonAttributeRepository;
import com.somemore.user.repository.usercommonattribute.record.UserProfileDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import java.util.List;

import static com.somemore.user.domain.UserRole.VOLUNTEER;
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
    private UserCommonAttribute userCommonAttribute;

    @BeforeEach
    void setup() {
        UserAuthInfo userAuthInfo = UserAuthInfo.createForOAuth(OAuthProvider.NAVER);

        user = userRepository.save(User.of(userAuthInfo, UserRole.VOLUNTEER));
        userCommonAttribute = userCommonAttributeRepository.save(UserCommonAttribute.createDefault(user.getId(), UserRole.VOLUNTEER));
    }


    @DisplayName("유저 아이디로 유저를 조회할 수 있다.")
    @Test
    void getById() {
        // given

        // when
        User foundUser = userQueryService.getById(user.getId());

        // then
        assertThat(foundUser)
                .isNotNull()
                .isEqualTo(user);

    }

    @DisplayName("유저 아이디로 유저 권한을 조회할 수 있다.")
    @Test
    void getRoleById() {
        // given

        // when
        UserRole role = userQueryService.getRoleById(user.getId());

        // then
        assertThat(role)
                .isNotNull()
                .isEqualTo(user.getRole());
    }

    @DisplayName("유저 계정 아이디로 유저를 조회할 수 있다.")
    @Test
    void getByAccountId() {
        // given

        // when
        User foundUser = userQueryService.getByAccountId(user.getAccountId());

        // then
        assertThat(foundUser)
                .isNotNull()
                .isEqualTo(user);
    }

    @DisplayName("유저 아이디로 유저 공통 속성을 조회할 수 있다.")
    @Test
    void getCommonAttributeByUserID() {
        // given

        // when
        UserCommonAttribute foundCommonAttribute = userQueryService.getCommonAttributeByUserId(user.getId());

        // then
        assertThat(foundCommonAttribute)
                .isNotNull()
                .isEqualTo(userCommonAttribute);
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

    @DisplayName("유저 id로 유저 프로필에 필요한 공통 속성을 조회할 수 있다.")
    @Test
    void getUserProfileByUserId() {

        //given
        UUID userId = UUID.randomUUID();
        UserRole role = UserRole.VOLUNTEER;

        UserCommonAttribute userCommonAttribute1 = UserCommonAttribute.createDefault(userId, role);
        userCommonAttributeRepository.save(userCommonAttribute1);

        //when
        UserProfileDto result = userQueryService.getUserProfileByUserId(userId);

        //then
        assertThat(result).isNotNull();
        assertThat(result.id()).isNotNull();
        assertThat(result.userId()).isEqualTo(userId);
        assertThat(result.name()).isEqualTo(userCommonAttribute1.getName());
        assertThat(result.contactNumber()).isEqualTo(userCommonAttribute1.getContactNumber());
        assertThat(result.imgUrl()).isEqualTo(userCommonAttribute1.getImgUrl());
        assertThat(result.introduce()).isEqualTo(userCommonAttribute1.getIntroduce());
    }

    @DisplayName("유저 아이디(userId) 리스트로 유저 공통 속성 리스트를 조회할 수 있다.")
    @Test
    void findAllByUserIds() {
        // given
        UserCommonAttribute one = createUserCommonAttribute();
        UserCommonAttribute two = createUserCommonAttribute();
        UserCommonAttribute three = createUserCommonAttribute();

        userCommonAttributeRepository.save(one);
        userCommonAttributeRepository.save(two);
        userCommonAttributeRepository.save(three);

        List<UUID> userIds = List.of(one.getUserId(), two.getUserId(), three.getUserId());

        // when
        List<UserCommonAttribute> result = userQueryService.getAllByUserIds(userIds);

        // then
        assertThat(result).hasSize(3);
        assertThat(result)
                .extracting(UserCommonAttribute::getUserId)
                .containsExactlyInAnyOrder(one.getUserId(), two.getUserId(), three.getUserId());

    }

    private UserCommonAttribute createUserCommonAttribute() {
        return UserCommonAttribute.createDefault(UUID.randomUUID(), VOLUNTEER);
    }
}
