package com.somemore.center.service;

import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_PREFER_ITEM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.somemore.center.domain.PreferItem;
import com.somemore.center.repository.preferitem.PreferItemRepository;
import com.somemore.global.exception.BadRequestException;
import com.somemore.support.IntegrationTestSupport;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class DeletePreferItemServiceTest extends IntegrationTestSupport {

    @Autowired
    private DeletePreferItemService deletePreferItemService;

    @Autowired
    private PreferItemRepository preferItemRepository;

    private UUID centerId;
    private PreferItem preferItem;

    @BeforeEach
    void setUp() {
        centerId = UUID.randomUUID();
        preferItem = createPreferItem(centerId);
        preferItemRepository.save(preferItem);
    }

    @DisplayName("기관은 자신의 선호 물품을 삭제할 수 있다.")
    @Test
    void deletePreferItem() {
        //given
        Long id = preferItem.getId();

        //when
        deletePreferItemService.deletePreferItem(centerId, id);

        //then
        Optional<PreferItem> deletedItem = preferItemRepository.findById(id);
        assertThat(deletedItem).isEmpty();
    }

    @DisplayName("선호 물품을 등록한 기관이 아니라면 선호 물품을 삭제할 수 없다")
    @Test
    void deletePreferItemUnauthorized() {
        //given
        Long id = preferItem.getId();
        UUID wrongCenterId = UUID.randomUUID();

        // when
        // then
        assertThatThrownBy(
                () -> deletePreferItemService.deletePreferItem(wrongCenterId, id))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(UNAUTHORIZED_PREFER_ITEM.getMessage());
    }

    private PreferItem createPreferItem(UUID centerId) {
        return PreferItem.create(centerId, "선호 물품");
    }
}
