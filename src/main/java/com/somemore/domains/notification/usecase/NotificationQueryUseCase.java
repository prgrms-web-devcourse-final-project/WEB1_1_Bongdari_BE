package com.somemore.domains.notification.usecase;

import com.somemore.domains.notification.domain.Notification;
import com.somemore.domains.notification.dto.NotificationResponseDto;

import java.util.List;
import java.util.UUID;

public interface NotificationQueryUseCase {

    Notification getNotification(Long id);

    List<Notification> getNotifications(UUID userId);

    List<Notification> getNotifications(List<Long> ids);

    List<NotificationResponseDto> getUnreadNotifications(UUID userId);

    List<NotificationResponseDto> getReadNotifications(UUID userId);
}
