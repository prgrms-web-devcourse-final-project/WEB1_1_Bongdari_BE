package com.somemore.interestcenter.handler;

import com.somemore.support.IntegrationTestSupport;
import com.somemore.global.common.event.ServerEventPublisher;
import com.somemore.interestcenter.event.domain.InterestCenterCreateRecruitBoardEvent;
import com.somemore.interestcenter.event.handler.CreateRecruitBoardHandlerImpl;
import com.somemore.interestcenter.usecase.InterestCenterQueryUseCase;
import com.somemore.recruitboard.event.CreateRecruitBoardEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Transactional
class CreateRecruitBoardHandlerImplTest extends IntegrationTestSupport {

    @Autowired
    private CreateRecruitBoardHandlerImpl createRecruitBoardHandler;

    @MockBean
    private InterestCenterQueryUseCase interestCenterQueryUseCase;

    @MockBean
    private ServerEventPublisher serverEventPublisher;

    @Test
    void handle_ShouldPublishEventsForVolunteers() {
        // given
        UUID centerId = UUID.randomUUID();
        UUID volunteerId1 = UUID.randomUUID();
        UUID volunteerId2 = UUID.randomUUID();
        UUID volunteerId3 = UUID.randomUUID();
        Long recruitBoardId = 123L;

        List<UUID> volunteerIds = List.of(volunteerId1, volunteerId2, volunteerId3);
        when(interestCenterQueryUseCase.getVolunteerIdsByCenterId(centerId)).thenReturn(volunteerIds);

        CreateRecruitBoardEvent createRecruitBoardEvent = CreateRecruitBoardEvent.builder()
                .centerId(centerId)
                .recruitBoardId(recruitBoardId)
                .build();

        // when
        createRecruitBoardHandler.handle(createRecruitBoardEvent);

        // then
        verify(serverEventPublisher, times(volunteerIds.size())).publish(Mockito.any(InterestCenterCreateRecruitBoardEvent.class));

        for (UUID volunteerId : volunteerIds) {
            verify(serverEventPublisher).publish(argThat(event ->
                    event instanceof InterestCenterCreateRecruitBoardEvent &&
                            ((InterestCenterCreateRecruitBoardEvent) event).getCenterId().equals(centerId) &&
                            ((InterestCenterCreateRecruitBoardEvent) event).getVolunteerId().equals(volunteerId) &&
                            ((InterestCenterCreateRecruitBoardEvent) event).getRecruitBoardId().equals(recruitBoardId)
            ));
        }
    }
}
