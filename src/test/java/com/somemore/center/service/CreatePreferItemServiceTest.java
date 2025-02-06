package com.somemore.center.service;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_CENTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.domain.PreferItem;
import com.somemore.center.dto.request.PreferItemCreateRequestDto;
import com.somemore.center.dto.response.PreferItemCreateResponseDto;
import com.somemore.center.repository.NEWCenterRepository;
import com.somemore.center.repository.preferitem.PreferItemRepository;
import com.somemore.global.exception.NoSuchElementException;
import com.somemore.support.IntegrationTestSupport;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Transactional
class CreatePreferItemServiceTest extends IntegrationTestSupport {

    @Autowired
    private CreatePreferItemService createPreferItemService;

    @Autowired
    private PreferItemRepository preferItemRepository;

    @Autowired
    private NEWCenterRepository centerRepository;

    @DisplayName("기관 아이디와 선호물품 이름을 받아 선호물품을 생성한다.")
    @Test
    void createPreferItem() {
        // given
        NEWCenter center = createCenter();
        centerRepository.save(center);
        String itemName = "어린이 도서";

        PreferItemCreateRequestDto requestDto = new PreferItemCreateRequestDto(itemName);

        // when
        PreferItemCreateResponseDto dto = createPreferItemService.createPreferItem(
                center.getId(), requestDto);

        // then
        Optional<PreferItem> findItem = preferItemRepository.findById(dto.id());
        assertThat(findItem).isPresent();
        assertThat(findItem.get().getItemName()).isEqualTo(itemName);
    }

    @DisplayName("존재하지 않는 기관 아이디로 선호물품을 등록하려고 하면 예외를 발생시킨다.")
    @Test
    void createPreferItemThrowsExceptionWhenCenterDoesNotExist() {
        // given
        UUID invalidCenterId = UUID.randomUUID();
        String itemName = "어린이 도서";

        PreferItemCreateRequestDto requestDto = new PreferItemCreateRequestDto(
                itemName
        );

        // when & then
        assertThatThrownBy(
                () -> createPreferItemService.createPreferItem(invalidCenterId, requestDto))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining(NOT_EXISTS_CENTER.getMessage());
    }

    private NEWCenter createCenter() {
        return NEWCenter.createDefault(UUID.randomUUID());
    }

}
