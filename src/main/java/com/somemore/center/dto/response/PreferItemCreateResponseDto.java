package com.somemore.center.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.center.domain.PreferItem;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Builder;

@JsonNaming(SnakeCaseStrategy.class)
@Builder
public record PreferItemCreateResponseDto(
        @Schema(description = "선호물품의 ID", example = "111")
        Long id,
        @Schema(description = "기관의 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID centerId,
        @Schema(description = "선호물품 이름", example = "어린이 도서")
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

