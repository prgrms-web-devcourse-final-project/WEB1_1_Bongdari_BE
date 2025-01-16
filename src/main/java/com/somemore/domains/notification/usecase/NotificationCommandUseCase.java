package com.somemore.domains.notification.usecase;

import com.somemore.domains.notification.dto.NotificationIdsRequestDto;

import java.util.UUID;

public interface NotificationCommandUseCase {

    void markSingleNotificationAsRead(UUID userId, Long notificationId);

    void markMultipleNotificationsAsRead(UUID userId, NotificationIdsRequestDto notificationIds);
}
