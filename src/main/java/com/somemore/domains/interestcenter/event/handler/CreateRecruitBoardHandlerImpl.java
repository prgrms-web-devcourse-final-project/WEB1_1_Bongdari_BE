package com.somemore.domains.interestcenter.event.handler;

import com.somemore.domains.interestcenter.event.domain.InterestCenterCreateRecruitBoardEvent;
import com.somemore.domains.interestcenter.usecase.InterestCenterQueryUseCase;
import com.somemore.domains.recruitboard.event.CreateRecruitBoardEvent;
import com.somemore.global.common.event.ServerEventPublisher;
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
        InterestCenterCreateRecruitBoardEvent event = InterestCenterCreateRecruitBoardEvent.of(createRecruitBoardEvent, volunteerId, centerId);
        serverEventPublisher.publish(event);
    }
}
