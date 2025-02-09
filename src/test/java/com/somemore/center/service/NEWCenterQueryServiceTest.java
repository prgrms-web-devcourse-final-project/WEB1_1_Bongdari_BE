package com.somemore.center.service;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_CENTER;
import static com.somemore.user.domain.UserRole.CENTER;
import static org.assertj.core.api.Assertions.*;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.domain.PreferItem;
import com.somemore.center.dto.response.CenterProfileResponseDto;
import com.somemore.center.dto.response.PreferItemResponseDto;
import com.somemore.center.repository.NEWCenterRepository;
import com.somemore.center.repository.preferitem.PreferItemJpaRepository;
import com.somemore.center.usecase.PreferItemQueryUseCase;
import com.somemore.global.exception.ExceptionMessage;
import com.somemore.global.exception.NoSuchElementException;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.repository.usercommonattribute.UserCommonAttributeRepository;
import java.util.List;
import java.util.UUID;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class NEWCenterQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private NEWCenterQueryService centerQueryService;

    @Autowired
    private NEWCenterRepository centerRepository;

    @Autowired
    private PreferItemQueryUseCase preferItemQueryUseCase;

    @Autowired
    private UserCommonAttributeRepository userCommonAttributeRepository;

    @Autowired
    private PreferItemJpaRepository preferItemRepository;

    @Test
    @DisplayName("사용자 ID로 기관을 조회한다")
    void getByUserId() {
        //given
        UUID userId = UUID.randomUUID();
        NEWCenter center = NEWCenter.createDefault(userId);
        centerRepository.save(center);

        //when
        NEWCenter foundCenter = centerQueryService.getByUserId(userId);

        //then
        assertThat(foundCenter).isEqualTo(center);
    }

    @Test
    @DisplayName("사용자 ID로 기관 ID를 조회한다")
    void getIdByUserId() {
        //given
        UUID userId = UUID.randomUUID();
        NEWCenter center = NEWCenter.createDefault(userId);
        centerRepository.save(center);

        // when
        UUID foundCenterId = centerQueryService.getIdByUserId(userId);

        //then
        assertThat(foundCenterId).isEqualTo(center.getId());
    }

    @DisplayName("기관의 유저 ID로 기관 정보를 조회할 수 있다.")
    @Test
    void getCenterProfileByUserId() {
        //given
        UUID userId = UUID.randomUUID();
        NEWCenter center = NEWCenter.createDefault(userId);
        centerRepository.save(center);

        UserCommonAttribute userCommonAttribute = UserCommonAttribute.createDefault(userId, CENTER);
        userCommonAttributeRepository.save(userCommonAttribute);

        PreferItem preferItem = PreferItem.create(center.getId(), "어린이 동화책");
        PreferItem preferItem1 = PreferItem.create(center.getId(), "간식");
        preferItemRepository.saveAll(List.of(preferItem, preferItem1));

        List<PreferItemResponseDto> preferItems = preferItemQueryUseCase.getPreferItemDtosByCenterId(
                center.getId());

        //when
        CenterProfileResponseDto result = centerQueryService.getCenterProfileById(center.getId());

        //then
        assertThat(result).isNotNull();
        assertThat(result)
                .extracting("id", "userId", "homepageUrl", "name", "contactNumber", "imgUrl",
                        "introduce", "preferItems")
                .containsExactly(
                        center.getId(),
                        userId,
                        center.getHomepageUrl(),
                        userCommonAttribute.getName(),
                        userCommonAttribute.getContactNumber(),
                        userCommonAttribute.getImgUrl(),
                        userCommonAttribute.getIntroduce(),
                        preferItems
                );
    }

    @DisplayName("아이디로 기관 존재유무를 검증할 수 있다.")
    @Test
    void validateCenterExists() {
        //given
        NEWCenter center = NEWCenter.createDefault(UUID.randomUUID());
        centerRepository.save(center);

        // when
        // then
        assertThatCode(
                () -> centerQueryService.validateCenterExists(center.getId()))
                .doesNotThrowAnyException();
    }

    @DisplayName("존재하지 않는 아이디로 기관 존재유무를 검증하면 에러가 발생한다.")
    @Test
    void validateCenterExistsWhenDoesNotExist() {
        //given
        UUID wrongId = UUID.randomUUID();

        // when
        // then
        assertThatThrownBy(
                () -> centerQueryService.validateCenterExists(wrongId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(NOT_EXISTS_CENTER.getMessage());
    }

    @DisplayName("기관 Id로 기관명을 조회할 수 있다. (service)")
    @Test
    void getNameById() {
        // given
        NEWCenter center = NEWCenter.createDefault(UUID.randomUUID());
        centerRepository.save(center);
        UserCommonAttribute userCommonAttribute = UserCommonAttribute.createDefault(center.getUserId(), CENTER);
        userCommonAttributeRepository.save(userCommonAttribute);

        // when
        String foundName = centerQueryService.getNameById(center.getId());

        // then
        assertThat(foundName).contains("기관");
    }

    @DisplayName("존재하지 않는 기관 id로 기관명 조회 시 예외가 발생한다. (service)")
    @Test
    void getNameByNonExistentId() {
        // given
        // when
        ThrowableAssert.ThrowingCallable callable = () -> centerQueryService.getNameById(
                UUID.randomUUID());

        // then
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(callable)
                .withMessage(ExceptionMessage.NOT_EXISTS_CENTER.getMessage());
    }
}
