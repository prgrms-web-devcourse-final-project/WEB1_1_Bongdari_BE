package com.somemore.user.repository.usercommonattribute;

import com.somemore.global.imageupload.service.ImageUploadService;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.domain.UserRole;
import com.somemore.user.repository.usercommonattribute.record.UserProfileDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class UserCommonAttributeRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private UserCommonAttributeRepositoryImpl userCommonAttributeRepository;

    @DisplayName("유저 공통 속성을 저장할 수 있다.")
    @Test
    void saveUserCommonAttribute() {
        // given
        UUID userId = UUID.randomUUID();
        UserCommonAttribute userCommonAttribute = UserCommonAttribute.createDefault(userId, UserRole.VOLUNTEER);

        // when
        UserCommonAttribute savedUserCommonAttribute = userCommonAttributeRepository.save(userCommonAttribute);

        // then
        assertThat(savedUserCommonAttribute)
                .isNotNull()
                .isEqualTo(userCommonAttribute);

        assertThat(savedUserCommonAttribute.getId()).isNotNull();
        assertThat(savedUserCommonAttribute.getUserId()).isEqualTo(userId);
        assertThat(savedUserCommonAttribute.getImgUrl()).isEqualTo(ImageUploadService.DEFAULT_IMAGE_URL);
        assertThat(savedUserCommonAttribute.isCustomized()).isFalse();
    }

    @DisplayName("유저 ID로 프로필에 필요한 유저 정보를 조회할 수 있다.")
    @Test
    void findUserProfileByUserId() {
        // given
        UUID userId = UUID.randomUUID();
        UserCommonAttribute userCommonAttribute = UserCommonAttribute.createDefault(userId, UserRole.VOLUNTEER);
        userCommonAttributeRepository.save(userCommonAttribute);

        // when
        Optional<UserProfileDto> result = userCommonAttributeRepository.findUserProfileByUserId(userId);

        // then
        assertThat(result).isPresent();
        UserProfileDto userProfile = result.get();

        assertThat(userProfile.id()).isNotNull();
        assertThat(userProfile.userId()).isEqualTo(userId);
        assertThat(userProfile.name()).isEqualTo(userCommonAttribute.getName());
        assertThat(userProfile.contactNumber()).isEqualTo(userCommonAttribute.getContactNumber());
        assertThat(userProfile.imgUrl()).isEqualTo(userCommonAttribute.getImgUrl());
        assertThat(userProfile.introduce()).isEqualTo(userCommonAttribute.getIntroduce());
    }
}
