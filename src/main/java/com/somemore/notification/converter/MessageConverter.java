package com.somemore.notification.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.somemore.notification.domain.Notification;
import com.somemore.notification.domain.NotificationSubType;
import com.somemore.volunteerapply.domain.ApplyStatus;
import com.somemore.volunteerapply.domain.VolunteerApplyStatusChangeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MessageConverter {

    private final ObjectMapper objectMapper;

    public Notification from(String message) {
        try {
            JsonNode rootNode = objectMapper.readTree(message);
            String eventType = rootNode.get("subType").asText();

            return switch (NotificationSubType.from(eventType)) {
                case NOTE_BLAH_BLAH -> throw new UnsupportedOperationException("NOTE 알림 타입 처리 로직 미구현");
                case REVIEW_BLAH_BLAH -> throw new UnsupportedOperationException("REVIEW 알림 타입 처리 로직 미구현");
                case VOLUNTEER_APPLY_STATUS_CHANGE -> buildVolunteerApplyStatusChangeNotification(message);
            };
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalStateException();
        }
    }

    private Notification buildVolunteerApplyStatusChangeNotification(String message) throws Exception {
        VolunteerApplyStatusChangeEvent event = objectMapper.readValue(message, VolunteerApplyStatusChangeEvent.class);

        return Notification.builder()
                .receiverId(event.getReceiverId())
                .title(buildNotificationTitle(event.getNewStatus()))
                .type(NotificationSubType.VOLUNTEER_APPLY_STATUS_CHANGE)
                .relatedId(event.getRecruitBoardId())
                .build();
    }

    private Notification handleNoteEvent(String message) {
        // TODO: NOTE 이벤트를 처리하는 로직 구현
        throw new UnsupportedOperationException("NOTE 알림 타입 처리 로직 미구현");
    }

    private Notification handleReviewEvent(String message) {
        // TODO: System 이벤트를 처리하는 로직 구현
        throw new UnsupportedOperationException("REVIEW 알림 타입 처리 로직 미구현");
    }

    private String buildNotificationTitle(ApplyStatus newStatus) {
        return switch (newStatus) {
            case APPROVED -> "봉사 활동 신청이 승인되었습니다.";
            case REJECTED -> "봉사 활동 신청이 거절되었습니다.";
            default -> "봉사 활동 신청 상태가 변경되었습니다.";
        };
    }
}
