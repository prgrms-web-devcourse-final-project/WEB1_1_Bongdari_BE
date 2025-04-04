package com.somemore.domains.notification.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.domains.notification.domain.Notification;
import com.somemore.domains.notification.domain.NotificationSubType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "알림 응답 DTO")
public record NotificationResponseDto(
        @Schema(description = "알림 ID", example = "1")
        Long id,
        @Schema(description = "알림 제목", example = "봉사 활동 신청이 승인되었습니다.")
        String title,

        @Schema(description = "알림 유형", example = "RECRUIT")
        NotificationSubType type,

        @Schema(description = "알림과 관련된 리소스 ID", example = "12345")
        Long relatedId,

        @Schema(description = "알림 생성 시간", example = "2024-12-04T10:15:30")
        LocalDateTime createdAt) {

    public static NotificationResponseDto from(Notification notification) {
        return new NotificationResponseDto(
                notification.getId(),
                notification.getTitle(),
                notification.getType(),
                notification.getRelatedId(),
                notification.getCreatedAt()
        );
    }

    public static List<NotificationResponseDto> from(List<Notification> notifications) {
        return notifications.stream()
                .map(NotificationResponseDto::from)
                .toList();
    }
}
