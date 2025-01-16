package com.somemore.user.service;

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
class ValidateBasicInfoServiceTest extends IntegrationTestSupport {

    @Autowired
    private ValidateBasicInfoService validateBasicInfoService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCommonAttributeRepository userCommonAttributeRepository;

    private User user;

    @BeforeEach
    void setup() {
        UserAuthInfo userAuthInfo = new UserAuthInfo("test@test.com", "Test User");
        user = userRepository.save(User.of(userAuthInfo, UserRole.VOLUNTEER));
    }

    @DisplayName("필수 입력 정보가 입력되었다면, true를 반환한다.")
    @Test
    void isBasicInfoComplete_ReturnsTrue() {
        // given
        UserCommonAttribute userCommonAttribute = UserCommonAttribute.createDefault(user.getId(), UserRole.VOLUNTEER);
        userCommonAttribute.customize();
        userCommonAttributeRepository.save(userCommonAttribute);

        // when
        boolean result = validateBasicInfoService.isBasicInfoComplete(user.getId());

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("필수 입력 정보가 입력되지 않았다면, false를 반환한다.")
    @Test
    void isNotBasicInfoComplete_ReturnsFalse() {
        // given
        UserCommonAttribute userCommonAttribute = UserCommonAttribute.createDefault(user.getId(), UserRole.VOLUNTEER);
        userCommonAttributeRepository.save(userCommonAttribute);

        // when
        boolean result = validateBasicInfoService.isBasicInfoComplete(user.getId());

        // then
        assertThat(result).isFalse();
    }
}
