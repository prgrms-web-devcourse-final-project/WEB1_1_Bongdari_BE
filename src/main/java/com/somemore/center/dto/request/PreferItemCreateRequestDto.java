package com.somemore.center.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.center.domain.PreferItem;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PreferItemCreateRequestDto(

        @Schema(description = "선호물품 이름", example = "어린이 도서")
        @NotNull(message = "물품 이름은 필수값입니다.")
        String itemName

) {

    public PreferItem toEntity(UUID centerId) {
        return PreferItem.create(centerId, itemName);
    }

}
