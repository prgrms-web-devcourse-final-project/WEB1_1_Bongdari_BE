package com.somemore.notification.service;

import com.somemore.global.exception.BadRequestException;
import com.somemore.notification.domain.Notification;
import com.somemore.notification.dto.NotificationIdsRequestDto;
import com.somemore.notification.repository.NotificationRepository;
import com.somemore.notification.usecase.NotificationCommandUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_NOTIFICATION;
import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_NOTIFICATION;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationCommandService implements NotificationCommandUseCase {

    private final NotificationRepository notificationRepository;

    @Override
    public void markSingleNotificationAsRead(UUID userId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BadRequestException(NOT_EXISTS_NOTIFICATION));

        validateNotificationOwnership(userId, notification.getReceiverId());

        notification.markAsRead();
    }

    @Override
    public void markMultipleNotificationsAsRead(UUID userId, NotificationIdsRequestDto notificationIds) {
        List<Notification> notifications = notificationRepository.findAllByIds(notificationIds.ids());

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
