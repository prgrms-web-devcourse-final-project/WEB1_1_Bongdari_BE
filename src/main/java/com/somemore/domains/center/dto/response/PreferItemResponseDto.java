package com.somemore.domains.center.dto.response;

import com.somemore.domains.center.domain.PreferItem;
import lombok.Builder;

import java.util.UUID;

@Builder
public record PreferItemResponseDto(
        Long id,
        UUID centerId,
        String itemName
) {
    public static PreferItemResponseDto from(PreferItem preferItem) {
        return PreferItemResponseDto.builder()
                .id(preferItem.getId())
                .centerId(preferItem.getCenterId())
                .itemName(preferItem.getItemName())
                .build();
    }
}
