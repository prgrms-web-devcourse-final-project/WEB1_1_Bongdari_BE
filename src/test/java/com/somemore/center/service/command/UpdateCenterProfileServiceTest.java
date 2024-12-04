package com.somemore.center.service.command;

import com.somemore.IntegrationTestSupport;
import com.somemore.center.domain.Center;
import com.somemore.center.dto.request.CenterProfileUpdateRequestDto;
import com.somemore.center.repository.CenterRepository;
import com.somemore.global.exception.BadRequestException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.common.fixture.CenterFixture.createCenter;
import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_CENTER;
import static org.assertj.core.api.Assertions.*;

@Transactional
public class UpdateCenterProfileServiceTest extends IntegrationTestSupport {

    @Autowired
    private UpdateCenterProfileService updateCenterProfileService;

    @Autowired
    private CenterRepository centerRepository;

    CenterProfileUpdateRequestDto requestDto = CenterProfileUpdateRequestDto.builder()
            .name("테스트 센터명")
            .contactNumber("010-0000-0000")
            .homepageLink("https://www.test.com")
            .introduce("테스트 설명")
            .build();

    String imgUrl = "http://example.com/updated-image.jpg";

    @Test
    @DisplayName("센터 프로필을 업데이트 한다.")
    void updateCenterProfile() {
        //given
        Center center = createCenter();
        centerRepository.save(center);

        //when
        updateCenterProfileService.updateCenterProfile(center.getId(), requestDto, imgUrl);

        //then
        Center updatedCenter = centerRepository.findCenterById(center.getId()).orElseThrow();
        assertThat(updatedCenter.getName()).isEqualTo(requestDto.name());
        assertThat(updatedCenter.getContactNumber()).isEqualTo(requestDto.contactNumber());
        assertThat(updatedCenter.getHomepageLink()).isEqualTo(requestDto.homepageLink());
        assertThat(updatedCenter.getIntroduce()).isEqualTo(requestDto.introduce());
        assertThat(updatedCenter.getImgUrl()).isEqualTo(imgUrl);
    }

    @Test
    @DisplayName("존재하지 않는 센터 ID로 업데이트 시 예외를 던진다")
    void updateCenterProfileWithInvalidId() {
        //given
        //when
        ThrowableAssert.ThrowingCallable callable = () -> updateCenterProfileService.updateCenterProfile(UUID.randomUUID(), requestDto, imgUrl);

        //then
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(callable)
                .withMessage(NOT_EXISTS_CENTER.getMessage());
    }
}
