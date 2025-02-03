package com.somemore.domains.notification.service;

import com.somemore.domains.notification.domain.Notification;
import com.somemore.domains.notification.dto.NotificationResponseDto;
import com.somemore.domains.notification.repository.NotificationRepository;
import com.somemore.domains.notification.usecase.NotificationQueryUseCase;
import com.somemore.global.exception.ExceptionMessage;
import com.somemore.global.exception.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationQueryService implements NotificationQueryUseCase {

    private final NotificationRepository notificationRepository;

    @Override
    public Notification getNotification(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(ExceptionMessage.NOT_EXISTS_NOTIFICATION));
    }

    @Override
    public List<Notification> getNotifications(UUID userId) {
        return notificationRepository.findAllByUserId(userId);
    }

    @Override
    public List<Notification> getNotifications(List<Long> ids) {
        return notificationRepository.findAllByIds(ids);
    }

    @Override
    public List<NotificationResponseDto> getUnreadNotifications(UUID userId) {
        List<Notification> notifications = getNotifications(userId).stream()
                .filter(Predicate.not(Notification::isRead))
                .toList();
        return NotificationResponseDto.from(notifications);
    }

    @Override
    public List<NotificationResponseDto> getReadNotifications(UUID userId) {
        List<Notification> notifications = getNotifications(userId).stream()
                .filter(Notification::isRead)
                .toList();
        return NotificationResponseDto.from(notifications);
    }
}
