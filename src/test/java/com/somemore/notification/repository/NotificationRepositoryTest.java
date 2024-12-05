package com.somemore.notification.repository;

import com.somemore.IntegrationTestSupport;
import com.somemore.notification.domain.Notification;
import com.somemore.notification.domain.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class NotificationRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private NotificationRepository notificationRepository;

    private UUID receiverId;

    @BeforeEach
    void setup() {
        receiverId = UUID.randomUUID();

        for (int i = 0; i < 10; i++) {
            Notification unreadNotification = Notification.builder()
                    .title("Unread Notification")
                    .type(NotificationType.NOTE_BLAH_BLAH)
                    .receiverId(receiverId)
                    .relatedId(1L)
                    .build();

            Notification readNotification = Notification.builder()
                    .title("Read Notification")
                    .type(NotificationType.REVIEW_BLAH_BLAH)
                    .receiverId(receiverId)
                    .relatedId(2L)
                    .build();

            readNotification.markAsRead();

            notificationRepository.save(unreadNotification);
            notificationRepository.save(readNotification);
        }
    }

    @DisplayName("사용자의 읽지 않은 알림을 조회한다.")
    @Test
    void findByReceiverIdAndUnread() {
        // when
        List<Notification> notifications = notificationRepository.findByReceiverIdAndUnread(receiverId);

        // then
        assertThat(notifications).hasSize(10);
        assertThat(notifications.getFirst().getTitle()).isEqualTo("Unread Notification");
        assertThat(notifications.getFirst().isRead()).isFalse();
    }

    @DisplayName("사용자의 읽은 알림을 조회한다.")
    @Test
    void findByReceiverIdAndRead() {
        // when
        List<Notification> notifications = notificationRepository.findByReceiverIdAndRead(receiverId);

        // then
        assertThat(notifications).hasSize(10);
        assertThat(notifications.getFirst().getTitle()).isEqualTo("Read Notification");
        assertThat(notifications.getFirst().isRead()).isTrue();
    }

    @DisplayName("알림이 없는 사용자의 읽지 않은 알림을 조회하면 빈 리스트를 반환한다.")
    @Test
    void findByReceiverIdAndUnread_noNotifications() {
        // given
        UUID unknownReceiverId = UUID.randomUUID();

        // when
        List<Notification> notifications = notificationRepository.findByReceiverIdAndUnread(unknownReceiverId);

        // then
        assertThat(notifications).isEmpty();
    }

    @DisplayName("알림이 없는 사용자의 읽은 알림을 조회하면 빈 리스트를 반환한다.")
    @Test
    void findByReceiverIdAndRead_noNotifications() {
        // given
        UUID unknownReceiverId = UUID.randomUUID();

        // when
        List<Notification> notifications = notificationRepository.findByReceiverIdAndRead(unknownReceiverId);

        // then
        assertThat(notifications).isEmpty();
    }
}
