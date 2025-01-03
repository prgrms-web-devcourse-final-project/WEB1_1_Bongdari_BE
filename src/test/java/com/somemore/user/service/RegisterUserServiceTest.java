package com.somemore.user.service;

import com.somemore.global.auth.oauth.domain.CommonOAuthInfo;
import com.somemore.global.auth.oauth.domain.OAuthProvider;
import com.somemore.global.imageupload.service.ImageUploadService;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.user.domain.User;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.domain.UserRole;
import com.somemore.user.domain.UserSetting;
import com.somemore.user.dto.UserAuthInfo;
import com.somemore.user.repository.user.UserRepository;
import com.somemore.user.repository.usercommonattribute.UserCommonAttributeRepository;
import com.somemore.user.repository.usersetting.UserSettingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class RegisterUserServiceTest extends IntegrationTestSupport {

    @Autowired
    private RegisterUserService registerUserService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSettingRepository userSettingRepository;

    @Autowired
    private UserCommonAttributeRepository userCommonAttributeRepository;

    @DisplayName("OAuth 사용자를 기본 ROLE인 봉사자로 등록할 수 있다.")
    @Test
    void registerOAuthUser() {
        // given
        OAuthProvider provider = OAuthProvider.NAVER;
        CommonOAuthInfo commonOAuthInfo = new CommonOAuthInfo(provider, "test");

        // when
        User registeredUser = registerUserService.registerOAuthUser(commonOAuthInfo, UserRole.getOAuthUserDefaultRole());

        // then
        User savedUser = userRepository.findById(registeredUser.getId()).orElseThrow();
        UserSetting savedSetting = userSettingRepository.findByUserId(registeredUser.getId()).orElseThrow();
        UserCommonAttribute savedCommonAttribute = userCommonAttributeRepository.findByUserId(registeredUser.getId()).orElseThrow();

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getRole()).isEqualTo(UserRole.getOAuthUserDefaultRole());
        assertThat(savedUser.getEmail())
                .hasSize(UUID.randomUUID().toString().length() + provider.getProviderName().length());
        assertThat(savedSetting).isNotNull();
        assertThat(savedSetting.getUserId()).isEqualTo(savedUser.getId());
        boolean userDefaultSetting = false;
        assertThat(savedSetting.isSmsAgreed()).isEqualTo(userDefaultSetting);
        assertThat(savedSetting.isAuthenticated()).isEqualTo(userDefaultSetting);

        assertThat(savedCommonAttribute).isNotNull();
        assertThat(savedCommonAttribute.getUserId()).isEqualTo(savedUser.getId());
        assertThat(savedCommonAttribute.getNickname()).hasSize(8);
        assertThat(savedCommonAttribute.getIntroduce()).isEqualTo("");
        assertThat(savedCommonAttribute.getImgUrl()).isEqualTo(ImageUploadService.DEFAULT_IMAGE_URL);
        assertThat(savedCommonAttribute.isCustomized()).isEqualTo(userDefaultSetting);

    }

    @DisplayName("로컬 사용자를 등록할 수 있다.")
    @Test
    void registerLocalUser() {
        // given
        UserAuthInfo userAuthInfo = new UserAuthInfo("test", "test");
        UserRole role = UserRole.CENTER;

        // when
        User registeredUser = registerUserService.registerLocalUser(userAuthInfo, role);

        // then
        User savedUser = userRepository.findById(registeredUser.getId()).orElseThrow();
        UserSetting savedSetting = userSettingRepository.findByUserId(registeredUser.getId()).orElseThrow();
        UserCommonAttribute savedCommonAttribute = userCommonAttributeRepository.findByUserId(registeredUser.getId()).orElseThrow();

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getRole()).isEqualTo(role);
        assertThat(savedUser.getEmail()).isEqualTo("test");

        assertThat(savedSetting).isNotNull();
        assertThat(savedSetting.getUserId()).isEqualTo(savedUser.getId());
        boolean userDefaultSetting = false;
        assertThat(savedSetting.isSmsAgreed()).isEqualTo(userDefaultSetting);
        assertThat(savedSetting.isAuthenticated()).isEqualTo(userDefaultSetting);

        assertThat(savedCommonAttribute).isNotNull();
        assertThat(savedCommonAttribute.getUserId()).isEqualTo(savedUser.getId());
        assertThat(savedCommonAttribute.getNickname()).hasSize(8);
        assertThat(savedCommonAttribute.getIntroduce()).isEqualTo("");
        assertThat(savedCommonAttribute.getImgUrl()).isEqualTo(ImageUploadService.DEFAULT_IMAGE_URL);
        assertThat(savedCommonAttribute.isCustomized()).isEqualTo(userDefaultSetting);
    }
}
