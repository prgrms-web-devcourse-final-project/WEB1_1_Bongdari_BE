package com.somemore.domains.volunteerrecord.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.volunteerapply.domain.VolunteerApply;
import com.somemore.global.common.event.DomainEventSubType;
import com.somemore.global.common.event.ServerEvent;
import com.somemore.global.common.event.ServerEventType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@SuperBuilder
public class VolunteerRecordCreateEvent extends ServerEvent<DomainEventSubType> {

    private final UUID volunteerId;
    private final String title;
    private final LocalDate volunteerDate;
    private final int volunteerHours;

    @JsonCreator
    private VolunteerRecordCreateEvent(
            @JsonProperty(value = "volunteerId", required = true) UUID volunteerId,
            @JsonProperty(value = "title", required = true) String title,
            @JsonProperty(value = "volunteerDate", required = true) LocalDate volunteerDate,
            @JsonProperty(value = "volunteerHours", required = true) int volunteerHours) {

        super(ServerEventType.DOMAIN_EVENT, DomainEventSubType.VOLUNTEER_HOURS_SETTLE, LocalDateTime.now());

        this.volunteerId = volunteerId;
        this.title = title;
        this.volunteerDate = volunteerDate;
        this.volunteerHours = volunteerHours;
    }

    public static VolunteerRecordCreateEvent of(VolunteerApply apply, RecruitBoard recruitBoard) {
        return VolunteerRecordCreateEvent
                .builder()
                .type(ServerEventType.DOMAIN_EVENT)
                .subType(DomainEventSubType.VOLUNTEER_HOURS_SETTLE)
                .volunteerId(apply.getVolunteerId())
                .title(recruitBoard.getTitle())
                .volunteerDate(recruitBoard.getRecruitmentInfo().getVolunteerEndDateTime().toLocalDate())
                .volunteerHours(recruitBoard.getRecruitmentInfo().getVolunteerHours())
                .build();
    }
}
