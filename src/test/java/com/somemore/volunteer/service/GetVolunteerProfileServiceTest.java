package com.somemore.volunteer.service;

import com.somemore.global.exception.NoSuchElementException;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.domain.UserRole;
import com.somemore.user.repository.usercommonattribute.UserCommonAttributeRepository;
import com.somemore.user.usecase.UserQueryUseCase;
import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.dto.VolunteerProfileResponseDto;
import com.somemore.volunteer.repository.NEWVolunteerRepository;
import com.somemore.volunteer.usecase.NEWVolunteerQueryUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class GetVolunteerProfileServiceTest extends IntegrationTestSupport {

    @Autowired
    private GetVolunteerProfileService getVolunteerProfileService;

    @Autowired
    private NEWVolunteerQueryUseCase volunteerQueryUseCase;

    @Autowired
    private UserQueryUseCase userQueryUseCase;

    @Autowired
    private NEWVolunteerRepository volunteerRepository;

    @Autowired
    private UserCommonAttributeRepository userCommonAttributeRepository;

    @Test
    @DisplayName("성공적으로 봉사자 프로필을 조회할 수 있다")
    void testGetVolunteerProfileSuccess() {
        // given
        UUID userId = UUID.randomUUID();
        NEWVolunteer expectedVolunteer = NEWVolunteer.createDefault(userId);
        UserCommonAttribute expectedCommonAttribute = UserCommonAttribute.createDefault(userId, UserRole.VOLUNTEER);
        volunteerRepository.save(expectedVolunteer);
        userCommonAttributeRepository.save(expectedCommonAttribute);

        // when
        VolunteerProfileResponseDto volunteerProfileResponseDto = getVolunteerProfileService.getProfileByUserId(userId);

        // then
        assertThat(volunteerProfileResponseDto)
                .isNotNull();
        assertThat(volunteerProfileResponseDto.nickname())
                .isEqualTo(expectedVolunteer.getNickname());
        assertThat(volunteerProfileResponseDto.tier())
                .isEqualTo(expectedVolunteer.getTier().name());
        assertThat(volunteerProfileResponseDto.imgUrl())
                .isEqualTo(expectedCommonAttribute.getImgUrl());
        assertThat(volunteerProfileResponseDto.introduce())
                .isEqualTo(expectedCommonAttribute.getIntroduce());
        assertThat(volunteerProfileResponseDto.totalVolunteerHours())
                .isEqualTo(expectedVolunteer.getTotalVolunteerHours());
        assertThat(volunteerProfileResponseDto.totalVolunteerCount())
                .isEqualTo(expectedVolunteer.getTotalVolunteerCount());

        VolunteerProfileResponseDto.Detail detail = volunteerProfileResponseDto.detail();

        assertThat(detail.name())
                .isEqualTo(expectedCommonAttribute.getName());
        assertThat(detail.gender())
                .isEqualTo(expectedVolunteer.getGender().name());
        assertThat(detail.contactNumber())
                .isEqualTo(expectedCommonAttribute.getContactNumber());
    }

    @Test
    @DisplayName("존재하지 않는 봉사자 프로필 조회 시 예외를 발생시킨다")
    void testGetVolunteerProfileNotFound() {
        // given
        UUID nonExistUserId = UUID.randomUUID();

        // when
        // then
        assertThatThrownBy(() -> volunteerQueryUseCase.getByUserId(nonExistUserId))
                .isInstanceOf(NoSuchElementException.class);
    }

}