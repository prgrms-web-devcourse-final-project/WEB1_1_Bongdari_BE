package com.somemore.notification.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.somemore.community.event.CommentAddedEvent;
import com.somemore.facade.event.VolunteerReviewRequestEvent;
import com.somemore.notification.domain.Notification;
import com.somemore.notification.domain.NotificationSubType;
import com.somemore.volunteerapply.domain.ApplyStatus;
import com.somemore.volunteerapply.event.VolunteerApplyStatusChangeEvent;
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
                case VOLUNTEER_REVIEW_REQUEST -> buildVolunteerReviewRequestNotification(message);
                case VOLUNTEER_APPLY_STATUS_CHANGE -> buildVolunteerApplyStatusChangeNotification(message);
                case COMMENT_ADDED -> buildCommentAddedNotification(message);
            };
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalStateException();
        }
    }

    private Notification buildVolunteerReviewRequestNotification(String message) throws JsonProcessingException {
        VolunteerReviewRequestEvent event = objectMapper.readValue(message, VolunteerReviewRequestEvent.class);

        return Notification.builder()
                .receiverId(event.getVolunteerId())
                .title(createVolunteerReviewRequestNotificationTitle())
                .type(NotificationSubType.VOLUNTEER_REVIEW_REQUEST)
                .relatedId(event.getRecruitBoardId())
                .build();
    }

    private Notification buildVolunteerApplyStatusChangeNotification(String message) throws JsonProcessingException {
        VolunteerApplyStatusChangeEvent event = objectMapper.readValue(message, VolunteerApplyStatusChangeEvent.class);

        return Notification.builder()
                .receiverId(event.getVolunteerId())
                .title(createVolunteerApplyStatusChangeNotificationTitle(event.getNewStatus()))
                .type(NotificationSubType.VOLUNTEER_APPLY_STATUS_CHANGE)
                .relatedId(event.getRecruitBoardId())
                .build();
    }

    private Notification buildCommentAddedNotification(String message) throws JsonProcessingException {
        CommentAddedEvent event = objectMapper.readValue(message, CommentAddedEvent.class);

        return Notification.builder()
                .receiverId(event.getVolunteerId())
                .title(createCommentAddedNotificationTitle())
                .type(NotificationSubType.COMMENT_ADDED)
                .relatedId(event.getCommunityBoardId())
                .build();
    }

    private String createVolunteerReviewRequestNotificationTitle() {
        return "최근 활동하신 활동의 후기를 작성해 주세요!";
    }

    private String createVolunteerApplyStatusChangeNotificationTitle(ApplyStatus newStatus) {
        return switch (newStatus) {
            case APPROVED -> "봉사 활동 신청이 승인되었습니다.";
            case REJECTED -> "봉사 활동 신청이 거절되었습니다.";
            default -> {
                log.error("올바르지 않은 봉사 신청 상태입니다: {}", newStatus);
                throw new IllegalArgumentException();
            }
        };
    }

    private String createCommentAddedNotificationTitle() {
        return "새로운 댓글이 작성되었습니다.";
    }
}
