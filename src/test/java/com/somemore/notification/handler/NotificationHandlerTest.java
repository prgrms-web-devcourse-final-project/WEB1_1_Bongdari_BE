package com.somemore.notification.handler;

import com.somemore.IntegrationTestSupport;
import com.somemore.notification.converter.NotificationMessageConverter;
import com.somemore.notification.domain.Notification;
import com.somemore.notification.domain.NotificationSubType;
import com.somemore.notification.repository.NotificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class NotificationHandlerTest extends IntegrationTestSupport {

    @Autowired
    private NotificationHandlerImpl notificationHandler;

    @Autowired
    private NotificationMessageConverter notificationMessageConverter;

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    @DisplayName("handle 호출 시 Notification이 저장된다")
    void handle() {
        // given
        String message = """
                {
                    "type": "NOTIFICATION",
                    "subType": "VOLUNTEER_APPLY_STATUS_CHANGE",
                    "volunteerId": "123e4567-e89b-12d3-a456-426614174000",
                    "volunteerApplyId": 123,
                    "centerId": "123e4567-e89b-12d3-a456-426614174001",
                    "recruitBoardId": 456,
                    "oldStatus": "WAITING",
                    "newStatus": "APPROVED",
                    "createdAt": "2024-12-05T10:00:00"
                }
                """;

        UUID receiverId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        Notification notification = notificationMessageConverter.from(message);
        // when
        notificationHandler.handle(notification);

        // then
        List<Notification> notifications = notificationRepository.findByReceiverIdAndUnread(receiverId);
        assertThat(notifications).hasSize(1);

        Notification savedNotification = notifications.getFirst();
        assertThat(savedNotification.getReceiverId()).isEqualTo(receiverId);
        assertThat(savedNotification.getTitle()).isEqualTo("봉사 활동 신청이 승인되었습니다.");
        assertThat(savedNotification.getType()).isEqualTo(NotificationSubType.VOLUNTEER_APPLY_STATUS_CHANGE);
        assertThat(savedNotification.getRelatedId()).isEqualTo(456L); // 프론트 요구사항: 123L(봉사신청아이디), 456L(모집글아이디)
        assertThat(savedNotification.isRead()).isFalse();
        assertThat(savedNotification.getCreatedAt()).isEqualTo(notification.getCreatedAt());
        assertThat(savedNotification.getCreatedAt()).isNotNull();
    }

}
