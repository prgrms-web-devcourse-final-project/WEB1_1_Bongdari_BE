package com.somemore.domains.notification.converter;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.repository.NEWCenterRepository;
import com.somemore.domains.notification.domain.Notification;
import com.somemore.domains.notification.domain.NotificationSubType;
import com.somemore.domains.notification.event.converter.NotificationMessageConverter;
import com.somemore.global.auth.oauth.domain.OAuthProvider;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.user.domain.User;
import com.somemore.user.domain.UserAuthInfo;
import com.somemore.user.domain.UserRole;
import com.somemore.user.repository.user.UserRepository;
import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.repository.NEWVolunteerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
class NotificationMessageConverterTest extends IntegrationTestSupport {

    @Autowired
    private NotificationMessageConverter notificationMessageConverter;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NEWVolunteerRepository volunteerRepository;

    @Autowired
    private NEWCenterRepository centerRepository;

    @Test
    @DisplayName("VOLUNTEER_REVIEW_REQUEST 메시지를 변환하면 Notification 객체를 반환한다. 알림 수신자인 volunteerId가 userId로 변환된다.")
    void testVolunteerReviewRequestConversion() {
        // given
        UserAuthInfo volunteerUserAuthInfo = UserAuthInfo.createForOAuth(OAuthProvider.NAVER);
        User volunteerUser = User.of(volunteerUserAuthInfo, UserRole.VOLUNTEER);
        userRepository.save(volunteerUser);

        UserAuthInfo centerUserAuthInfo = UserAuthInfo.createForOAuth(OAuthProvider.NAVER);
        User centerUser = User.of(centerUserAuthInfo, UserRole.CENTER);
        userRepository.save(centerUser);

        NEWVolunteer volunteer = NEWVolunteer.createDefault(volunteerUser.getId());
        volunteerRepository.save(volunteer);

        NEWCenter center = NEWCenter.createDefault(centerUser.getId());
        centerRepository.save(center);

        String message = String.format("""
                {
                    "type": "NOTIFICATION",
                    "subType": "VOLUNTEER_REVIEW_REQUEST",
                    "volunteerId": "%s",
                    "volunteerApplyId": "1",
                    "centerId": "%s",
                    "recruitBoardId": 456,
                    "createdAt": "2024-12-05T10:00:00"
                }
                """, volunteer.getId(), center.getId());

        // when
        Notification notification = notificationMessageConverter.from(message);

        // then
        assertThat(notification.getReceiverId()).isEqualTo(volunteer.getUserId());
        assertThat(notification.getTitle()).isEqualTo("최근 활동하신 활동의 후기를 작성해 주세요!");
        assertThat(notification.getType()).isEqualTo(NotificationSubType.VOLUNTEER_REVIEW_REQUEST);
        assertThat(notification.getRelatedId()).isEqualTo(456L);
    }

    @Test
    @DisplayName("임의의 필드가 추가된 VOLUNTEER_APPLY_STATUS_CHANGE 메시지를 변환해도 Notification 객체를 반환한다")
    void testVolunteerApplyStatusChangeConversion() {
        // given
        UserAuthInfo volunteerUserAuthInfo = UserAuthInfo.createForOAuth(OAuthProvider.NAVER);
        User volunteerUser = User.of(volunteerUserAuthInfo, UserRole.VOLUNTEER);
        userRepository.save(volunteerUser);

        UserAuthInfo centerUserAuthInfo = UserAuthInfo.createForOAuth(OAuthProvider.NAVER);
        User centerUser = User.of(centerUserAuthInfo, UserRole.CENTER);
        userRepository.save(centerUser);

        NEWVolunteer volunteer = NEWVolunteer.createDefault(volunteerUser.getId());
        volunteerRepository.save(volunteer);

        NEWCenter center = NEWCenter.createDefault(centerUser.getId());
        centerRepository.save(center);

        String message = String.format("""
                {
                    "extraField": "this should be ignored",
                    "extraField": "this should be ignored",
                    "extraField": "this should be ignored",
                    "extraField": "this should be ignored",
                    "extraField": "this should be ignored",
                    "type": "NOTIFICATION",
                    "subType": "VOLUNTEER_APPLY_STATUS_CHANGE",
                    "volunteerId": "%s",
                    "centerId": "%s",
                    "volunteerApplyId": "1",
                    "recruitBoardId": 456,
                    "oldStatus": "WAITING",
                    "newStatus": "APPROVED",
                    "createdAt": "2024-12-05T10:00:00"
                }
                """, volunteer.getId(), center.getId());

        // when
        Notification notification = notificationMessageConverter.from(message);

        // then
        assertThat(notification.getReceiverId()).isEqualTo(volunteer.getUserId());
        assertThat(notification.getTitle()).isEqualTo("봉사 활동 신청이 승인되었습니다.");
        assertThat(notification.getType()).isEqualTo(NotificationSubType.VOLUNTEER_APPLY_STATUS_CHANGE);
        assertThat(notification.getRelatedId()).isEqualTo(456L);
    }

    @Test
    @DisplayName("INTEREST_CENTER_CREATE_RECRUIT_BOARD 메시지를 변환하면 Notification 객체를 반환한다. ")
    void testBuildInterestCenterCreateRecruitBoardNotification() {
        // given
        UserAuthInfo volunteerUserAuthInfo = UserAuthInfo.createForOAuth(OAuthProvider.NAVER);
        User volunteerUser = User.of(volunteerUserAuthInfo, UserRole.VOLUNTEER);
        userRepository.save(volunteerUser);

        UserAuthInfo centerUserAuthInfo = UserAuthInfo.createForOAuth(OAuthProvider.NAVER);
        User centerUser = User.of(centerUserAuthInfo, UserRole.CENTER);
        userRepository.save(centerUser);

        NEWVolunteer volunteer = NEWVolunteer.createDefault(volunteerUser.getId());
        volunteerRepository.save(volunteer);

        NEWCenter center = NEWCenter.createDefault(centerUser.getId());
        centerRepository.save(center);

        String message = String.format("""
                {
                    "type": "NOTIFICATION",
                    "subType": "INTEREST_CENTER_CREATE_RECRUIT_BOARD",
                    "volunteerId": "%s",
                    "centerId": "%s",
                    "recruitBoardId": 456,
                    "createdAt": "2024-12-05T10:00:00"
                }
                """, volunteer.getId(), center.getId());

        // when
        Notification notification = notificationMessageConverter.from(message);

        // then
        assertThat(notification.getReceiverId()).isEqualTo(volunteer.getUserId());
        assertThat(notification.getTitle()).isEqualTo("관심 기관이 봉사 모집을 등록했습니다.");
        assertThat(notification.getType()).isEqualTo(NotificationSubType.INTEREST_CENTER_CREATE_RECRUIT_BOARD);
        assertThat(notification.getRelatedId()).isEqualTo(456L);
    }

    @Test
    @DisplayName("잘못된 JSON 메시지를 변환하면 IllegalStateException을 던진다")
    void testInvalidJson() {
        // given
        String invalidMessage = "{ invalid-json }";

        // when
        // then
        assertThrows(IllegalStateException.class, () -> notificationMessageConverter.from(invalidMessage));
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
        assertThrows(IllegalStateException.class, () -> notificationMessageConverter.from(messageWithMissingFields));
    }
}
