package com.somemore.domains.center.service.query;

import com.somemore.domains.center.domain.Center;
import com.somemore.domains.center.domain.PreferItem;
import com.somemore.domains.center.dto.response.CenterProfileResponseDto;
import com.somemore.domains.center.repository.center.CenterJpaRepository;
import com.somemore.domains.center.repository.center.CenterRepository;
import com.somemore.domains.center.repository.mapper.CenterOverviewInfo;
import com.somemore.domains.center.repository.preferitem.PreferItemJpaRepository;
import com.somemore.global.exception.BadRequestException;
import com.somemore.global.exception.ExceptionMessage;
import com.somemore.support.IntegrationTestSupport;
import jakarta.transaction.Transactional;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@Transactional
class CenterQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private CenterQueryService centerQueryService;

    @Autowired
    private CenterRepository centerRepository;

    @Autowired
    private CenterJpaRepository centerJpaRepository;
    
    @Autowired
    private PreferItemJpaRepository preferItemRepository;

    @DisplayName("기관 Id로 기관 프로필을 조회할 수 있다. (service)")
    @Test
    void getCenterProfileById() {
        // given
        Center center = createCenter();
        Center foundCenter = centerRepository.save(center);

        PreferItem preferItem = PreferItem.create(foundCenter.getId(), "어린이 동화책");
        PreferItem preferItem1 = PreferItem.create(foundCenter.getId(), "간식");
        preferItemRepository.saveAll(List.of(preferItem, preferItem1));

        // when
        CenterProfileResponseDto responseDto = centerQueryService.getCenterProfileByCenterId(foundCenter.getId());

        // then
        assertThat(responseDto)
                .extracting(
                        "centerId",
                        "name",
                        "contactNumber",
                        "imgUrl",
                        "introduce",
                        "homepageLink"
                )
                .containsExactly(
                        foundCenter.getId(),
                        "기본 기관 이름",
                        "010-1234-5678",
                        "http://example.com/image.jpg",
                        "기관 소개 내용",
                        "http://example.com"
                );

        assertThat(responseDto.preferItems())
                .hasSize(2)
                .extracting("itemName")
                .containsExactly("어린이 동화책", "간식");
    }

    @DisplayName("기관 Id들로 기관의 오버뷰 정보를 조회할 수 있다.")
    @Test
    void getCenterOverviewsByIds() {
        //given
        Center center = createCenter();
        Center center1 = createCenter();
        Center center2 = createCenter();
        centerJpaRepository.saveAll(List.of(center, center1, center2));
        List<UUID> ids = List.of(center.getId(),center1.getId(), center2.getId());

        //when
        List<CenterOverviewInfo> responseDtos = centerQueryService.getCenterOverviewsByIds(ids);

        //then
        assertThat(responseDtos)
                .isNotNull()
                .hasSize(3)
                .extracting("centerId", "centerName", "imgUrl")
                .containsExactlyInAnyOrder(
                        tuple(center.getId(), center.getName(), center.getImgUrl()),
                        tuple(center1.getId(), center1.getName(), center1.getImgUrl()),
                        tuple(center2.getId(), center2.getName(), center2.getImgUrl())
                );
    }

    @DisplayName("존재하지 않는 기관 ID를 검증할 수 있다.")
    @Test
    void validateNonExistentCenter() {
        // given
        UUID nonExistentId = UUID.randomUUID();

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () ->
                centerQueryService.validateCenterExists(nonExistentId);

        // then
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(throwingCallable)
                .withMessage(ExceptionMessage.NOT_EXISTS_CENTER.getMessage());
    }

    @DisplayName("존재하는 기관 ID로 조회 시 예외가 발생하지 않는다")
    @Test
    void validateExistingCenter() {
        // given
        Center center = Center.create(
                "기본 기관 이름",
                "010-1234-5678",
                "http://example.com/image.jpg",
                "기관 소개 내용",
                "http://example.com"
        );
        Center savedCenter = centerRepository.save(center);

        // when
        ThrowableAssert.ThrowingCallable callable = () -> centerQueryService.validateCenterExists(savedCenter.getId());

        // then
        assertThatCode(callable).doesNotThrowAnyException();
    }

    @DisplayName("기관 Id로 기관명을 조회할 수 있다. (service)")
    @Test
    void getNameById() {
        // given
        Center center = createCenter();
        Center foundCenter = centerRepository.save(center);

        // when
        String foundName = centerQueryService.getNameById(foundCenter.getId());

        // then
        assertThat(foundName).isEqualTo("기본 기관 이름");
    }

    @DisplayName("존재하지 않는 기관 id로 기관명 조회 시 예외가 발생한다. (service)")
    @Test
    void getNameByNonExistentId() {
        // given
        // when
        ThrowableAssert.ThrowingCallable callable = () -> centerQueryService.getNameById(UUID.randomUUID());

        // then
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(callable)
                .withMessage(ExceptionMessage.NOT_EXISTS_CENTER.getMessage());
    }

    private Center createCenter() {
        return Center.create(
                "기본 기관 이름",
                "010-1234-5678",
                "http://example.com/image.jpg",
                "기관 소개 내용",
                "http://example.com"
        );
    }
}
