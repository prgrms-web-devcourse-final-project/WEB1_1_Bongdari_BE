package com.somemore.user.repository.usercommonattribute;

import com.somemore.global.imageupload.service.ImageUploadService;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.user.domain.UserCommonAttribute;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
        UserCommonAttribute userCommonAttribute = UserCommonAttribute.createDefault(userId);

        // when
        UserCommonAttribute savedUserCommonAttribute = userCommonAttributeRepository.save(userCommonAttribute);

        // then
        assertThat(savedUserCommonAttribute).isNotNull();
        assertThat(savedUserCommonAttribute.getId()).isNotNull();
        assertThat(savedUserCommonAttribute.getUserId()).isEqualTo(userId);
        assertThat(savedUserCommonAttribute.getImgUrl()).isEqualTo(ImageUploadService.DEFAULT_IMAGE_URL);
        assertThat(savedUserCommonAttribute.isCustomized()).isEqualTo(false);

    }
}
