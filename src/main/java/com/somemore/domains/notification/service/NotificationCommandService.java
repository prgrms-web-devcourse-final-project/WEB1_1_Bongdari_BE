package com.somemore.domains.notification.service;

import com.somemore.domains.notification.domain.Notification;
import com.somemore.domains.notification.dto.NotificationIdsRequestDto;
import com.somemore.domains.notification.usecase.NotificationCommandUseCase;
import com.somemore.domains.notification.usecase.NotificationQueryUseCase;
import com.somemore.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_NOTIFICATION;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationCommandService implements NotificationCommandUseCase {

    private final NotificationQueryUseCase notificationQueryUseCase;

    @Override
    public void markSingleNotificationAsRead(UUID userId, Long notificationId) {
        Notification notification = notificationQueryUseCase.getNotification(notificationId);

        validateNotificationOwnership(userId, notification.getReceiverId());

        notification.markAsRead();
    }

    @Override
    public void markMultipleNotificationsAsRead(UUID userId, NotificationIdsRequestDto notificationIds) {
        List<Notification> notifications = notificationQueryUseCase.getNotifications(notificationIds.ids());

        notifications.forEach(notification ->
                validateNotificationOwnership(userId, notification.getReceiverId()));

        notifications.forEach(Notification::markAsRead);
    }

    private void validateNotificationOwnership(UUID userId, UUID receiverId) {
        if (!receiverId.equals(userId)) {
            throw new BadRequestException(UNAUTHORIZED_NOTIFICATION);
        }
    }

}
