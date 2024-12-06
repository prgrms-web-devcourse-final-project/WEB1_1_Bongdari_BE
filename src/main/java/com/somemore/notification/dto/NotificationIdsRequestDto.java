package com.somemore.notification.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record NotificationIdsRequestDto(

        @Schema(description = "읽음 처리할 알림 아이디 리스트",
                example = "[1, 2, 3, 4, 5]")
        List<Long> ids
) {
}
