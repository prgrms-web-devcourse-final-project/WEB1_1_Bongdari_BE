package com.somemore.center.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.center.domain.PreferItem;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Builder;

@JsonNaming(SnakeCaseStrategy.class)
@Builder
public record PreferItemResponseDto(
        @Schema(description = "선호 물품 ID", example = "1")
        Long id,
        @Schema(description = "기관 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID centerId,
        @Schema(description = "선호 물품명", example = "쌀20kg")
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
