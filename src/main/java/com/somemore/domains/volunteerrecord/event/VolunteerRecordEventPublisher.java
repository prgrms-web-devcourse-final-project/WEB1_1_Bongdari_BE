package com.somemore.domains.volunteerrecord.event;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.volunteerapply.domain.VolunteerApply;
import com.somemore.global.common.event.ServerEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class VolunteerRecordEventPublisher {

    private final ServerEventPublisher serverEventPublisher;

    public void publishVolunteerRecordCreateEvent(VolunteerApply apply, RecruitBoard recruitBoard) {

        VolunteerRecordCreateEvent event = VolunteerRecordCreateEvent.of(apply, recruitBoard);
        serverEventPublisher.publish(event);
    }
}
