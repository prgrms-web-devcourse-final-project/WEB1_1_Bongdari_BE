package com.somemore.center.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.center.domain.PreferItem;
import lombok.Builder;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record PreferItemCreateResponseDto(
        Long id,
        UUID centerId,
        String itemName
) {
    public static PreferItemCreateResponseDto from(PreferItem preferItem) {
        return PreferItemCreateResponseDto.builder()
                .id(preferItem.getId())
                .centerId(preferItem.getCenterId())
                .itemName(preferItem.getItemName())
                .build();
    }
}

