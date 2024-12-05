package com.somemore.notification.service;

import com.somemore.IntegrationTestSupport;
import com.somemore.notification.domain.Notification;
import com.somemore.notification.domain.NotificationType;
import com.somemore.notification.dto.NotificationResponseDto;
import com.somemore.notification.repository.NotificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class NotificationQueryServiceIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private NotificationQueryService notificationQueryService;

    @Autowired
    private NotificationRepository notificationRepository;

    @DisplayName("사용자의 읽지 않은 알림을 조회한다.")
    @Test
    void getUnreadNotifications() {
        // given
        UUID receiverId = UUID.randomUUID();

        Notification unreadNotification = Notification.builder()
                .title("Unread Notification")
                .type(NotificationType.NOTE_BLAH_BLAH)
                .receiverId(receiverId)
                .relatedId(1L)
                .build();

        notificationRepository.save(unreadNotification);

        // when
        List<NotificationResponseDto> unreadNotifications = notificationQueryService.getUnreadNotifications(receiverId);

        // then
        assertThat(unreadNotifications).hasSize(1);
        assertThat(unreadNotifications.getFirst().title()).isEqualTo("Unread Notification");
    }

    @DisplayName("사용자의 읽은 알림을 조회한다.")
    @Test
    void getReadNotifications() {
        // given
        UUID receiverId = UUID.randomUUID();

        Notification readNotification = Notification.builder()
                .title("Read Notification")
                .type(NotificationType.REVIEW_BLAH_BLAH)
                .receiverId(receiverId)
                .relatedId(2L)
                .build();
        readNotification.markAsRead();

        notificationRepository.save(readNotification);

        // when
        List<NotificationResponseDto> readNotifications = notificationQueryService.getReadNotifications(receiverId);

        // then
        assertThat(readNotifications).hasSize(1);
        assertThat(readNotifications.getFirst().title()).isEqualTo("Read Notification");
    }

    @DisplayName("읽지 않은 알림이 없는 경우 빈 리스트를 반환한다.")
    @Test
    void getUnreadNotifications_noNotifications() {
        // given
        UUID receiverId = UUID.randomUUID();

        // when
        List<NotificationResponseDto> unreadNotifications = notificationQueryService.getUnreadNotifications(receiverId);

        // then
        assertThat(unreadNotifications).isEmpty();
    }

    @DisplayName("읽은 알림이 없는 경우 빈 리스트를 반환한다.")
    @Test
    void getReadNotifications_noNotifications() {
        // given
        UUID receiverId = UUID.randomUUID();

        // when
        List<NotificationResponseDto> readNotifications = notificationQueryService.getReadNotifications(receiverId);

        // then
        assertThat(readNotifications).isEmpty();
    }
}