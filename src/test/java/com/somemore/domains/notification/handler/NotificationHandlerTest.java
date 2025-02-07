package com.somemore.domains.notification.handler;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.repository.NEWCenterRepository;
import com.somemore.domains.notification.domain.Notification;
import com.somemore.domains.notification.domain.NotificationSubType;
import com.somemore.domains.notification.event.converter.NotificationMessageConverter;
import com.somemore.domains.notification.event.handler.NotificationHandlerImpl;
import com.somemore.domains.notification.repository.NotificationRepository;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class NotificationHandlerTest extends IntegrationTestSupport {

    @Autowired
    private NotificationHandlerImpl notificationHandler;

    @Autowired
    private NotificationMessageConverter notificationMessageConverter;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NEWVolunteerRepository volunteerRepository;

    @Autowired
    private NEWCenterRepository centerRepository;

    @Test
    @DisplayName("handle 호출 시 Notification이 저장된다")
    void handle() {

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
                    "subType": "VOLUNTEER_APPLY_STATUS_CHANGE",
                    "volunteerId": "%s",
                    "volunteerApplyId": 123,
                    "centerId": "%s",
                    "recruitBoardId": 456,
                    "oldStatus": "WAITING",
                    "newStatus": "APPROVED",
                    "createdAt": "2024-12-05T10:00:00"
                }
                """, volunteer.getId(), center.getId());

        Notification notification = notificationMessageConverter.from(message);
        // when
        notificationHandler.handle(notification);

        // then
        List<Notification> notifications = notificationRepository.findByReceiverIdAndUnread(volunteerUser.getId());
        assertThat(notifications).hasSize(1);

        Notification savedNotification = notifications.getFirst();
        assertThat(savedNotification.getReceiverId()).isEqualTo(volunteerUser.getId());
        assertThat(savedNotification.getTitle()).isEqualTo("봉사 활동 신청이 승인되었습니다.");
        assertThat(savedNotification.getType()).isEqualTo(NotificationSubType.VOLUNTEER_APPLY_STATUS_CHANGE);
        assertThat(savedNotification.getRelatedId()).isEqualTo(456L); // 프론트 요구사항: 123L(봉사신청아이디), 456L(모집글아이디)
        assertThat(savedNotification.isRead()).isFalse();
        assertThat(savedNotification.getCreatedAt()).isEqualTo(notification.getCreatedAt());
        assertThat(savedNotification.getCreatedAt()).isNotNull();
    }

}
