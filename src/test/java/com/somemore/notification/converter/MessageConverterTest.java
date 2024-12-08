package com.somemore.notification.converter;

import com.somemore.IntegrationTestSupport;
import com.somemore.notification.domain.Notification;
import com.somemore.notification.domain.NotificationSubType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MessageConverterTest extends IntegrationTestSupport {

    @Autowired
    private MessageConverter messageConverter;

    @Test
    @DisplayName("VOLUNTEER_REVIEW_REQUEST 메시지를 변환하면 Notification 객체를 반환한다")
    void testVolunteerReviewRequestConversion() {
        // given
        String message = """
                {
                    "type": "NOTIFICATION",
                    "subType": "VOLUNTEER_REVIEW_REQUEST",
                    "volunteerId": "123e4567-e89b-12d3-a456-426614174000",
                    "volunteerApplyId": "1",
                    "centerId": "123e4567-e89b-12d3-a456-426614174001",
                    "recruitBoardId": 456,
                    "createdAt": "2024-12-05T10:00:00"
                }
                """;

        // when
        Notification notification = messageConverter.from(message);

        // then
        assertThat(notification.getReceiverId()).isEqualTo(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        assertThat(notification.getTitle()).isEqualTo("최근 활동하신 활동의 후기를 작성해 주세요!");
        assertThat(notification.getType()).isEqualTo(NotificationSubType.VOLUNTEER_REVIEW_REQUEST);
        assertThat(notification.getRelatedId()).isEqualTo(456L);
    }

    @Test
    @DisplayName("임의의 필드가 추가된 VOLUNTEER_APPLY_STATUS_CHANGE 메시지를 변환해도 Notification 객체를 반환한다")
    void testVolunteerApplyStatusChangeConversion() {
        // given
        String message = """
                {
                    "extraField": "this should be ignored",
                    "extraField": "this should be ignored",
                    "extraField": "this should be ignored",
                    "extraField": "this should be ignored",
                    "extraField": "this should be ignored",
                    "type": "NOTIFICATION",
                    "subType": "VOLUNTEER_APPLY_STATUS_CHANGE",
                    "volunteerId": "123e4567-e89b-12d3-a456-426614174000",
                    "centerId": "123e4567-e89b-12d3-a456-426614174001",
                    "volunteerApplyId": "1",
                    "recruitBoardId": 456,
                    "oldStatus": "WAITING",
                    "newStatus": "APPROVED",
                    "createdAt": "2024-12-05T10:00:00"
                }
                """;

        // when
        Notification notification = messageConverter.from(message);

        // then
        assertThat(notification.getReceiverId()).isEqualTo(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        assertThat(notification.getTitle()).isEqualTo("봉사 활동 신청이 승인되었습니다.");
        assertThat(notification.getType()).isEqualTo(NotificationSubType.VOLUNTEER_APPLY_STATUS_CHANGE);
        assertThat(notification.getRelatedId()).isEqualTo(456L);
    }

    @Test
    @DisplayName("잘못된 JSON 메시지를 변환하면 IllegalStateException을 던진다")
    void testInvalidJson() {
        // given
        String invalidMessage = "{ invalid-json }";

        // when
        // then
        assertThrows(IllegalStateException.class, () -> messageConverter.from(invalidMessage));
    }

    @Test
    @DisplayName("필수 필드가 누락된 메시지를 변환하면 IllegalStateException을 던진다")
    void testMissingFields() {
        // given
        String messageWithMissingFields = """
                {
                    "type": "NOTIFICATION",
                    "subType": "VOLUNTEER_REVIEW_REQUEST"
                }
                """;

        // when & then
        assertThrows(IllegalStateException.class, () -> messageConverter.from(messageWithMissingFields));
    }
}