package com.somemore.center.service;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.dto.request.CenterProfileImgUpdateRequestDto;
import com.somemore.center.repository.NEWCenterRepository;
import com.somemore.global.imageupload.usecase.ImageUploadUseCase;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.repository.usercommonattribute.UserCommonAttributeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.somemore.user.domain.UserRole.CENTER;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class UpdateCenterProfileImgServiceTest extends IntegrationTestSupport {

    @Autowired
    private UpdateCenterProfileImgService updateCenterProfileImgService;

    @Autowired
    private ImageUploadUseCase imageUploadUseCase;

    @Autowired
    private NEWCenterRepository centerRepository;

    @Autowired
    private UserCommonAttributeRepository userCommonAttributeRepository;

    @DisplayName("파일명을 받아 기관 프로필 이미지의 presignedUrl을 발급해준다.")
    @Test
    void updateCenterProfileImg() {

        //given
        UUID userId = UUID.randomUUID();

        NEWCenter center = NEWCenter.createDefault(userId);
        centerRepository.save(center);
        UUID centerId = center.getId();

        UserCommonAttribute userCommonAttribute = UserCommonAttribute.createDefault(userId, CENTER);
        userCommonAttributeRepository.save(userCommonAttribute);

        CenterProfileImgUpdateRequestDto requestDto = new CenterProfileImgUpdateRequestDto("test.png");

        //when
        String presignedUrl = updateCenterProfileImgService.updateCenterProfileImg(centerId, requestDto);

        //then
        assertThat(presignedUrl).isNotNull();

        Optional<UserCommonAttribute> updatedUserCommonAttribute = userCommonAttributeRepository.findByUserId(userId);
        assertThat(updatedUserCommonAttribute)
                .isNotNull();
        assertTrue(updatedUserCommonAttribute.get().getImgUrl().contains(requestDto.fileName()));

    }


}