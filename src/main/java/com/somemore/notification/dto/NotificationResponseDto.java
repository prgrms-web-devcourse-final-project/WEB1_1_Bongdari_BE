package com.somemore.notification.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.notification.domain.Notification;
import com.somemore.notification.domain.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record NotificationResponseDto(
        @Schema(description = "알림을 받을 사용자 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID receiverId,

        @Schema(description = "알림 유형", example = "NOTE(쪽지), RECRUIT_APPLY(신청)")
        NotificationType type,

        @Schema(description = "관련된 엔티티 ID", example = "101")
        Long relatedId,

        @Schema(description = "알림 읽음 여부", example = "false")
        boolean read,

        @Schema(description = "알림 생성 시간", example = "2024-12-03T10:15:30")
        LocalDateTime createdAt
) {

    public static NotificationResponseDto from(Notification notification) {
        return NotificationResponseDto.builder()
                .receiverId(notification.getReceiverId())
                .type(notification.getType())
                .relatedId(notification.getRelatedId())
                .read(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}