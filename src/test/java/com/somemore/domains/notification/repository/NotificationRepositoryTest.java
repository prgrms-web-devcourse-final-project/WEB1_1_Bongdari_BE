package com.somemore.domains.notification.repository;

import com.somemore.domains.notification.domain.Notification;
import com.somemore.domains.notification.domain.NotificationSubType;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class NotificationRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private NotificationRepository notificationRepository;

    private UUID receiverId;

    private List<Long> savedNotificationIds;

    @BeforeEach
    void setup() {
        receiverId = UUID.randomUUID();
        savedNotificationIds = new ArrayList<>();

        for (long i = 1; i <= 10; i++) {
            Notification unreadNotification = createNotification(i, false);
            Notification readNotification = createNotification(i * 100, true);

            notificationRepository.save(unreadNotification);
            notificationRepository.save(readNotification);

            savedNotificationIds.add(unreadNotification.getId()); // 저장된 ID 캡처
        }
    }

    @DisplayName("사용자의 읽지 않은 알림을 조회한다.")
    @Test
    void findByReceiverIdAndUnread() {
        // when
        List<Notification> notifications = notificationRepository.findByReceiverIdAndUnread(receiverId);

        // then
        assertThat(notifications).hasSize(10);
        assertThat(notifications.getFirst().isRead()).isFalse();
    }

    @DisplayName("사용자의 읽은 알림을 조회한다.")
    @Test
    void findByReceiverIdAndRead() {
        // when
        List<Notification> notifications = notificationRepository.findByReceiverIdAndRead(receiverId);

        // then
        assertThat(notifications).hasSize(10);
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

    @DisplayName("알림 아이디로 알림을 조회한다.")
    @Test
    void findById() {
        // given
        // when
        Optional<Notification> notifications = notificationRepository.findById(savedNotificationIds.getFirst());

        // then
        assertThat(notifications).isNotEmpty();
    }

    @DisplayName("알림이 없는 사용자의 읽은 알림을 조회하면 빈 리스트를 반환한다.")
    @Test
    void findAllByIds() {
        // given
        notificationRepository.deleteAllInBatch();

        // when
        List<Notification> notifications = notificationRepository.findAllByIds(savedNotificationIds);

        // then
        assertThat(notifications).isEmpty();
    }

    private Notification createNotification(long i, boolean isRead) {
        Notification notification = Notification.builder()
                .title("Notification")
                .type(NotificationSubType.NEW_NOTE)
                .receiverId(receiverId)
                .relatedId(i + 1)
                .build();

        if (isRead) {
            notification.markAsRead();
        }
        return notification;
    }
}
