package com.somemore.domains.notification.event.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.somemore.domains.community.event.CommentAddedEvent;
import com.somemore.domains.volunteerapply.event.VolunteerReviewRequestEvent;
import com.somemore.domains.interestcenter.event.domain.InterestCenterCreateRecruitBoardEvent;
import com.somemore.domains.notification.domain.Notification;
import com.somemore.domains.notification.domain.NotificationSubType;
import com.somemore.domains.volunteerapply.domain.ApplyStatus;
import com.somemore.domains.volunteerapply.event.VolunteerApplyEvent;
import com.somemore.domains.volunteerapply.event.VolunteerApplyStatusChangeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationMessageConverter {

    private final ObjectMapper objectMapper;

    public static final String SUB_TYPE = "subType";

    public Notification from(String message) {
        try {
            JsonNode rootNode = objectMapper.readTree(message);
            String eventType = rootNode.get(SUB_TYPE).asText();

            return switch (NotificationSubType.from(eventType)) {
                case NEW_NOTE -> buildNewNoteNotification(message);
                case VOLUNTEER_REVIEW_REQUEST -> buildVolunteerReviewRequestNotification(message);
                case VOLUNTEER_APPLY_STATUS_CHANGE -> buildVolunteerApplyStatusChangeNotification(message);
                case COMMENT_ADDED -> buildCommentAddedNotification(message);
                case VOLUNTEER_APPLY -> buildVolunteerApplyNotification(message);
                case INTEREST_CENTER_CREATE_RECRUIT_BOARD -> buildInterestCenterCreateRecruitBoardNotification(message);
            };
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalStateException();
        }
    }

    private Notification buildNewNoteNotification(String message) {
        throw new UnsupportedOperationException("NOTE 알림 타입 처리 로직 미구현" + message);
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

    private Notification buildVolunteerApplyNotification(String message) throws JsonProcessingException {
        VolunteerApplyEvent event = objectMapper.readValue(message, VolunteerApplyEvent.class);

        return Notification.builder()
                .receiverId(event.getCenterId())
                .title(createVolunteerApplyNotificationTitle())
                .type(NotificationSubType.VOLUNTEER_APPLY)
                .relatedId(event.getRecruitBoardId())
                .build();
    }

    private Notification buildInterestCenterCreateRecruitBoardNotification(String message) throws JsonProcessingException {
        InterestCenterCreateRecruitBoardEvent event = objectMapper.readValue(message, InterestCenterCreateRecruitBoardEvent.class);

        return Notification.builder()
                .receiverId(event.getVolunteerId())
                .title(createInterestCenterCreateRecruitBoardNotificationTitle())
                .type(NotificationSubType.INTEREST_CENTER_CREATE_RECRUIT_BOARD)
                .relatedId(event.getRecruitBoardId())
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

    private String createVolunteerApplyNotificationTitle() {
        return "봉사 활동 모집에 새로운 신청이 있습니다.";
    }

    private String createInterestCenterCreateRecruitBoardNotificationTitle() {
        return "관심 기관이 봉사 모집을 등록했습니다.";
    }
}
