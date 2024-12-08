package com.somemore.volunteerapply.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.somemore.global.common.event.ServerEvent;
import com.somemore.global.common.event.ServerEventType;
import com.somemore.notification.domain.NotificationSubType;
import com.somemore.volunteerapply.domain.ApplyStatus;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@SuperBuilder
public class VolunteerApplyStatusChangeEvent extends ServerEvent<NotificationSubType> {

    private final UUID volunteerId;
    private final Long volunteerApplyId;
    private final UUID centerId;
    private final Long recruitBoardId;
    private final ApplyStatus oldStatus;
    private final ApplyStatus newStatus;

    @JsonCreator
    public VolunteerApplyStatusChangeEvent(
            @JsonProperty(value = "volunteerId", required = true) UUID volunteerId,
            @JsonProperty(value = "volunteerApplyId", required = true) Long volunteerApplyId,
            @JsonProperty(value = "centerId", required = true) UUID centerId,
            @JsonProperty(value = "recruitBoardId", required = true) Long recruitBoardId,
            @JsonProperty(value = "oldStatus", required = true) ApplyStatus oldStatus,
            @JsonProperty(value = "newStatus", required = true) ApplyStatus newStatus
    ) {
        super(ServerEventType.NOTIFICATION, NotificationSubType.VOLUNTEER_APPLY_STATUS_CHANGE, LocalDateTime.now());
        this.volunteerId = volunteerId;
        this.volunteerApplyId = volunteerApplyId;
        this.centerId = centerId;
        this.recruitBoardId = recruitBoardId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }
}
