package com.somemore.user.service;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.repository.NEWCenterRepository;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.user.domain.User;
import com.somemore.user.domain.UserAuthInfo;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.domain.UserRole;
import com.somemore.user.dto.basicinfo.CenterBasicInfoRequestDto;
import com.somemore.user.dto.basicinfo.CommonBasicInfoRequestDto;
import com.somemore.user.dto.basicinfo.VolunteerBasicInfoRequestDto;
import com.somemore.user.repository.user.UserRepository;
import com.somemore.user.repository.usercommonattribute.UserCommonAttributeRepository;
import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.repository.NEWVolunteerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class UpdateBasicInfoServiceTest extends IntegrationTestSupport {

    @Autowired
    private UpdateBasicInfoService updateBasicInfoService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCommonAttributeRepository userCommonAttributeRepository;
    @Autowired
    private NEWVolunteerRepository volunteerRepository;
    @Autowired
    private NEWCenterRepository centerRepository;

    @Test
    @DisplayName("봉사자 기본 정보를 업데이트 할 수 있다.")
    void updateVolunteerBasicInfo() {
        // given
        UUID userId = UUID.randomUUID();
        UserAuthInfo authInfo = UserAuthInfo.of("test", "test");
        User user = User.of(authInfo, UserRole.VOLUNTEER);
        UserCommonAttribute userCommonAttribute = UserCommonAttribute.createDefault(userId, UserRole.VOLUNTEER);

        NEWVolunteer volunteer = NEWVolunteer.createDefault(userId);

        userRepository.save(user);
        userCommonAttributeRepository.save(userCommonAttribute);
        volunteerRepository.save(volunteer);

        CommonBasicInfoRequestDto commonBasicInfoRequestDto = createCommonBasicInfoRequestDto();

        VolunteerBasicInfoRequestDto volunteerBasicInfoRequestDto =
                new VolunteerBasicInfoRequestDto(
                        commonBasicInfoRequestDto,
                        "test",
                        "test");

        boolean customizedBeforeUpdate = userCommonAttributeRepository.findIsCustomizedByUserId(userId)
                .orElseThrow();

        // when
        updateBasicInfoService.update(userId, volunteerBasicInfoRequestDto);

        // then
        boolean customizedAfterUpdate = userCommonAttributeRepository.findIsCustomizedByUserId(userId)
                .orElseThrow();

        assertThat(customizedBeforeUpdate).isFalse();
        assertThat(customizedAfterUpdate).isTrue();
    }

    @Test
    @DisplayName("센터 기본 정보를 업데이트 할 수 있다.")
    void updateCenterBasicInfo() {
        // given
        UUID userId = UUID.randomUUID();
        UserAuthInfo authInfo = UserAuthInfo.of("centerTest", "centerTest");
        User user = User.of(authInfo, UserRole.CENTER);
        UserCommonAttribute userCommonAttribute = UserCommonAttribute.createDefault(userId, UserRole.CENTER);

        NEWCenter center = NEWCenter.createDefault(userId);

        userRepository.save(user);
        userCommonAttributeRepository.save(userCommonAttribute);
        centerRepository.save(center);

        CommonBasicInfoRequestDto commonBasicInfoRequestDto = createCommonBasicInfoRequestDto();

        CenterBasicInfoRequestDto centerBasicInfoRequestDto =
                new CenterBasicInfoRequestDto(
                        commonBasicInfoRequestDto,
                        "Main Center"
                );

        boolean customizedBeforeUpdate = userCommonAttributeRepository.findIsCustomizedByUserId(userId)
                .orElseThrow();

        // when
        updateBasicInfoService.update(userId, centerBasicInfoRequestDto);

        // then
        boolean customizedAfterUpdate = userCommonAttributeRepository.findIsCustomizedByUserId(userId)
                .orElseThrow();

        assertThat(customizedBeforeUpdate).isFalse();
        assertThat(customizedAfterUpdate).isTrue();
    }

    private static CommonBasicInfoRequestDto createCommonBasicInfoRequestDto() {
        return new CommonBasicInfoRequestDto(
                "test",
                "test",
                "test"
        );
    }
}
