package com.somemore.user.service;

import com.somemore.support.IntegrationTestSupport;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.dto.request.UpdateProfileImgUrlRequestDto;
import com.somemore.user.repository.usercommonattribute.UserCommonAttributeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static com.somemore.user.domain.UserRole.CENTER;
import static org.junit.jupiter.api.Assertions.*;

class UpdateProfileImgUrlServiceTest extends IntegrationTestSupport {

    @Autowired
    private UpdateProfileImgUrlService updateProfileImgUrlService;

    @Autowired
    private UserQueryService userQueryService;

    @Autowired
    private UserCommonAttributeRepository commonAttributeRepository;

    @DisplayName("프로필 이미지 링크를 업데이트 할 수 있다.")
    @Test
    void updateProfileImgUrl() {

        // given
        UUID userId = UUID.randomUUID();

        UserCommonAttribute userCommonAttribute = UserCommonAttribute.createDefault(userId, CENTER);
        commonAttributeRepository.save(userCommonAttribute);

        String profileImgUrl = "https://example.com/new_profile.jpg";

        UpdateProfileImgUrlRequestDto requestDto = new UpdateProfileImgUrlRequestDto(userId, profileImgUrl);

        // when
        updateProfileImgUrlService.updateProfileImgUrl(requestDto);

        // then
        UserCommonAttribute updatedUserCommonAttribute = userQueryService.getCommonAttributeByUserId(userId);

        assertEquals(profileImgUrl, updatedUserCommonAttribute.getImgUrl());

        assertEquals(userCommonAttribute.getName(), updatedUserCommonAttribute.getName());
        assertEquals(userCommonAttribute.getContactNumber(), updatedUserCommonAttribute.getContactNumber());
        assertEquals(userCommonAttribute.getIntroduce(), updatedUserCommonAttribute.getIntroduce());
        assertEquals(userCommonAttribute.isCustomized(), updatedUserCommonAttribute.isCustomized());
    }

}