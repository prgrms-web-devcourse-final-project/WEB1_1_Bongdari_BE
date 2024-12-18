package com.somemore.domains.notification.service;

import com.somemore.domains.notification.domain.Notification;
import com.somemore.domains.notification.dto.NotificationResponseDto;
import com.somemore.domains.notification.repository.NotificationRepository;
import com.somemore.domains.notification.usecase.NotificationQueryUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationQueryService implements NotificationQueryUseCase {

    private final NotificationRepository notificationRepository;

    @Override
    public List<NotificationResponseDto> getUnreadNotifications(UUID userId) {
        List<Notification> notifications = notificationRepository.findByReceiverIdAndUnread(userId);
        return dtoFrom(notifications);
    }

    @Override
    public List<NotificationResponseDto> getReadNotifications(UUID userId) {
        List<Notification> notifications = notificationRepository.findByReceiverIdAndRead(userId);
        return dtoFrom(notifications);
    }

    private static List<NotificationResponseDto> dtoFrom(List<Notification> notifications) {
        return notifications.stream()
                .map(NotificationResponseDto::from)
                .toList();
    }
}
