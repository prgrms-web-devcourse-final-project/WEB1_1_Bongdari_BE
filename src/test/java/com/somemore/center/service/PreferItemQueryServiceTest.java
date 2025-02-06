package com.somemore.center.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.somemore.center.domain.PreferItem;
import com.somemore.center.dto.response.PreferItemResponseDto;
import com.somemore.center.repository.preferitem.PreferItemRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PreferItemQueryServiceTest {

    @Mock
    private PreferItemRepository preferItemRepository;

    @InjectMocks
    private PreferItemQueryService preferItemQueryService;

    @Test
    void getPreferItemDtosByCenterId() {
        // Given
        UUID centerId = UUID.randomUUID();
        PreferItem preferItem1 = createPreferItem(centerId, "item1");
        PreferItem preferItem2 = createPreferItem(centerId, "item2");
        List<PreferItem> preferItems = List.of(preferItem1, preferItem2);

        when(preferItemRepository.findByCenterId(centerId)).thenReturn(preferItems);

        // When
        List<PreferItemResponseDto> result = preferItemQueryService.getPreferItemDtosByCenterId(
                centerId);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).centerId()).isEqualTo(centerId);
        assertThat(result.get(0).itemName()).isEqualTo("item1");

        assertThat(result.get(1).centerId()).isEqualTo(centerId);
        assertThat(result.get(1).itemName()).isEqualTo("item2");

        verify(preferItemRepository, times(1)).findByCenterId(centerId);
    }

    @Test
    void getPreferItemsByCenterId() {
        // Given
        UUID centerId = UUID.randomUUID();
        PreferItem preferItem1 = createPreferItem(centerId, "item1");
        PreferItem preferItem2 = createPreferItem(centerId, "item2");
        List<PreferItem> expectedItems = List.of(preferItem1, preferItem2);

        when(preferItemRepository.findByCenterId(centerId)).thenReturn(expectedItems);

        // When
        List<PreferItem> result = preferItemQueryService.getPreferItemsByCenterId(centerId);

        // Then
        assertThat(result).hasSize(2).isEqualTo(expectedItems);
        verify(preferItemRepository, times(1)).findByCenterId(centerId);
    }

    private PreferItem createPreferItem(UUID centerId, String itemName) {
        return PreferItem.builder()
                .centerId(centerId)
                .itemName(itemName)
                .build();
    }
}
