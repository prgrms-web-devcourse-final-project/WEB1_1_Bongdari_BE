package com.somemore.domains.volunteerrecord.event;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.volunteerapply.domain.VolunteerApply;
import com.somemore.global.common.event.DomainEventSubType;
import com.somemore.global.common.event.ServerEventPublisher;
import com.somemore.global.common.event.ServerEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class VolunteerRecordEventPublisher {

    private final ServerEventPublisher serverEventPublisher;

    public void publishVolunteerRecordCreateEvent(VolunteerApply apply, RecruitBoard recruitBoard) {

        VolunteerRecordCreateEvent event = VolunteerRecordCreateEvent
                .builder()
                .type(ServerEventType.DOMAIN_EVENT)
                .subType(DomainEventSubType.VOLUNTEER_HOURS_SETTLE)
                .volunteerId(apply.getVolunteerId())
                .title(recruitBoard.getTitle())
                .volunteerDate(recruitBoard.getRecruitmentInfo().getVolunteerEndDateTime().toLocalDate())
                .volunteerHours(recruitBoard.getRecruitmentInfo().getVolunteerHours())
                .build();

        serverEventPublisher.publish(event);
    }
}
