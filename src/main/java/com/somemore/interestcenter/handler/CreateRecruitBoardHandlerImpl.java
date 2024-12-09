package com.somemore.interestcenter.handler;

import com.somemore.global.common.event.ServerEventPublisher;
import com.somemore.global.common.event.ServerEventType;
import com.somemore.interestcenter.event.InterestCenterCreateRecruitBoardEvent;
import com.somemore.interestcenter.usecase.InterestCenterQueryUseCase;
import com.somemore.notification.domain.NotificationSubType;
import com.somemore.recruitboard.event.CreateRecruitBoardEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional
public class CreateRecruitBoardHandlerImpl implements CreateRecruitBoardHandler {

    private final InterestCenterQueryUseCase interestCenterQueryUseCase;
    private final ServerEventPublisher serverEventPublisher;

    @Override
    public void handle(CreateRecruitBoardEvent createRecruitBoardEvent) {
        UUID centerId = createRecruitBoardEvent.getCenterId();
        List<UUID> volunteerIdsByCenterId = interestCenterQueryUseCase.getVolunteerIdsByCenterId(centerId);

        volunteerIdsByCenterId.forEach(volunteerId ->
                publishInterestCenterCreateRecruitBoardEvent(createRecruitBoardEvent, volunteerId, centerId)
        );
    }

    private void publishInterestCenterCreateRecruitBoardEvent(CreateRecruitBoardEvent createRecruitBoardEvent, UUID volunteerId, UUID centerId) {
        InterestCenterCreateRecruitBoardEvent event = InterestCenterCreateRecruitBoardEvent.builder()
                .type(ServerEventType.NOTIFICATION)
                .subType(NotificationSubType.INTEREST_CENTER_CREATE_RECRUIT_BOARD)
                .volunteerId(volunteerId)
                .centerId(centerId)
                .recruitBoardId(createRecruitBoardEvent.getRecruitBoardId())
                .build();

        serverEventPublisher.publish(event);
    }
}
