package com.somemore.domains.interestcenter.handler;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.repository.NEWCenterRepository;
import com.somemore.domains.interestcenter.event.domain.InterestCenterCreateRecruitBoardEvent;
import com.somemore.domains.interestcenter.event.handler.CreateRecruitBoardHandlerImpl;
import com.somemore.domains.interestcenter.usecase.InterestCenterQueryUseCase;
import com.somemore.domains.recruitboard.event.CreateRecruitBoardEvent;
import com.somemore.global.auth.oauth.domain.OAuthProvider;
import com.somemore.global.common.event.ServerEventPublisher;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.user.domain.User;
import com.somemore.user.domain.UserAuthInfo;
import com.somemore.user.domain.UserRole;
import com.somemore.user.repository.user.UserRepository;
import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.repository.NEWVolunteerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Transactional
class CreateRecruitBoardHandlerImplTest extends IntegrationTestSupport {

    @Autowired
    private CreateRecruitBoardHandlerImpl createRecruitBoardHandler;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NEWVolunteerRepository volunteerRepository;

    @Autowired
    private NEWCenterRepository centerRepository;

    @MockBean
    private InterestCenterQueryUseCase interestCenterQueryUseCase;

    @MockBean
    private ServerEventPublisher serverEventPublisher;

    @Test
    void handle_ShouldPublishEventsForVolunteers() {
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

        Long recruitBoardId = 123L;

        when(interestCenterQueryUseCase.getVolunteerIdsByCenterId(center.getId())).thenReturn(Collections.singletonList(volunteer.getId()));

        CreateRecruitBoardEvent createRecruitBoardEvent = CreateRecruitBoardEvent.builder()
                .centerId(center.getId())
                .recruitBoardId(recruitBoardId)
                .build();

        // when
        createRecruitBoardHandler.handle(createRecruitBoardEvent);

        // then
        verify(serverEventPublisher, times(1)).publish(Mockito.any(InterestCenterCreateRecruitBoardEvent.class));

        verify(serverEventPublisher).publish(argThat(event ->
                event instanceof InterestCenterCreateRecruitBoardEvent &&
                        ((InterestCenterCreateRecruitBoardEvent) event).getRecruitBoardId().equals(recruitBoardId) &&
                        ((InterestCenterCreateRecruitBoardEvent) event).getVolunteerId().equals(volunteer.getId())
        ));

    }
}
