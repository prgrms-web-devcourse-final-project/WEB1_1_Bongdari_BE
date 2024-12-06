package com.somemore.notification.service;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_NOTIFICATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.somemore.IntegrationTestSupport;
import com.somemore.global.exception.BadRequestException;
import com.somemore.notification.domain.Notification;
import com.somemore.notification.domain.NotificationSubType;
import com.somemore.notification.dto.NotificationIdsRequestDto;
import com.somemore.notification.repository.NotificationRepository;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class NotificationCommandServiceTest extends IntegrationTestSupport {

    @Autowired
    private NotificationCommandService notificationCommandService;

    @Autowired
    private NotificationRepository notificationRepository;

    @DisplayName("알림 1개를 읽음 처리한다.")
    @Test
    void markSingleNotificationAsRead() {
        // given
        UUID receiverId = UUID.randomUUID();
        Notification notification = createNotification(receiverId);
        notificationRepository.save(notification);

        // when
        notificationCommandService.markSingleNotificationAsRead(receiverId, notification.getId());

        // then
        Notification updatedNotification = notificationRepository.findById(notification.getId()).orElseThrow();
        assertThat(updatedNotification.isRead()).isTrue();
    }

    @DisplayName("존재하지 않는 알림을 읽음 처리하려고 하면 에러가 발생한다.")
    @Test
    void markSingleNotificationAsReadWhenNotificationNotExists() {
        // given
        UUID receiverId = UUID.randomUUID();
        Long nonExistentNotificationId = 999L;

        // when / then
        assertThatThrownBy(() -> notificationCommandService.markSingleNotificationAsRead(receiverId, nonExistentNotificationId))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(NOT_EXISTS_NOTIFICATION.getMessage());
    }

    @DisplayName("다른 사용자의 알림을 읽음 처리하려고 하면 에러가 발생한다.")
    @Test
    void markSingleNotificationAsReadWhenUnauthorized() {
        // given
        UUID receiverId = UUID.randomUUID();
        UUID anotherUserId = UUID.randomUUID();
        Notification notification = createNotification(anotherUserId);
        notificationRepository.save(notification);

        // when / then
        assertThatThrownBy(() -> notificationCommandService.markSingleNotificationAsRead(receiverId, notification.getId()))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("권한이 없습니다");
    }

    @DisplayName("알림 N개를 읽음 처리한다.")
    @Test
    void markMultipleNotificationsAsRead() {
        // given
        UUID receiverId = UUID.randomUUID();
        List<Notification> notifications = List.of(
                createNotification(receiverId),
                createNotification(receiverId)
        );
        notifications.forEach(notificationRepository::save);

        List<Long> notificationIds = notifications.stream().map(Notification::getId).toList();
        NotificationIdsRequestDto requestDto = NotificationIdsRequestDto.builder()
                .ids(notificationIds)
                .build();

        // when
        notificationCommandService.markMultipleNotificationsAsRead(receiverId, requestDto);

        // then
        List<Notification> updatedNotifications = notificationRepository.findAllByIds(notificationIds);
        updatedNotifications.forEach(notification -> assertThat(notification.isRead()).isTrue());
    }

    @DisplayName("다른 사용자의 알림 N개를 읽음 처리하려고 하면 에러가 발생한다.")
    @Test
    void markMultipleNotificationsAsReadWhenUnauthorized() {
        // given
        UUID receiverId = UUID.randomUUID();
        UUID anotherUserId = UUID.randomUUID();
        List<Notification> notifications = List.of(
                createNotification(anotherUserId),
                createNotification(anotherUserId)
        );
        notifications.forEach(notificationRepository::save);

        List<Long> notificationIds = notifications.stream()
                .map(Notification::getId)
                .toList();
        NotificationIdsRequestDto requestDto = NotificationIdsRequestDto.builder()
                .ids(notificationIds)
                .build();

        // when / then
        assertThatThrownBy(() -> notificationCommandService.markMultipleNotificationsAsRead(receiverId, requestDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("권한이 없습니다");
    }

    private Notification createNotification(UUID receiverId) {
        return Notification.builder()
                .receiverId(receiverId)
                .title("Unread")
                .type(NotificationSubType.REVIEW_BLAH_BLAH)
                .relatedId(1L)
                .build();
    }
}