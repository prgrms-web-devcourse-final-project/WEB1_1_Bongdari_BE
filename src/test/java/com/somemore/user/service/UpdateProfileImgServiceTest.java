package com.somemore.user.service;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.repository.NEWCenterRepository;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.dto.request.ImgUrlRequestDto;
import com.somemore.user.repository.usercommonattribute.UserCommonAttributeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.somemore.user.domain.UserRole.CENTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
class UpdateProfileImgServiceTest extends IntegrationTestSupport {

    @Autowired
    private UpdateProfileImgUrlService updateCenterProfileImgService;

    @Autowired
    private NEWCenterRepository centerRepository;

    @Autowired
    private UserCommonAttributeRepository userCommonAttributeRepository;

    @DisplayName("파일명을 받아 프로필 이미지의 presignedUrl을 발급해준다.")
    @Test
    void updateCenterProfileImg() {

        //given
        UUID userId = UUID.randomUUID();

        NEWCenter center = NEWCenter.createDefault(userId);
        centerRepository.save(center);

        UserCommonAttribute userCommonAttribute = UserCommonAttribute.createDefault(userId, CENTER);
        userCommonAttributeRepository.save(userCommonAttribute);

        //when
        String presignedUrl = updateCenterProfileImgService.update(userId, new ImgUrlRequestDto("test.png"));

        //then
        assertThat(presignedUrl).isNotNull();

        Optional<UserCommonAttribute> updatedUserCommonAttribute = userCommonAttributeRepository.findByUserId(userId);
        assertThat(updatedUserCommonAttribute)
                .isNotNull();
        assertTrue(updatedUserCommonAttribute.get().getImgUrl().contains("test.png"));
    }
}