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
            @JsonProperty("volunteerId") UUID volunteerId,
            @JsonProperty("volunteerApplyId") Long volunteerApplyId,
            @JsonProperty("centerId") UUID centerId,
            @JsonProperty("recruitBoardId") Long recruitBoardId,
            @JsonProperty("oldStatus") ApplyStatus oldStatus,
            @JsonProperty("newStatus") ApplyStatus newStatus
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
