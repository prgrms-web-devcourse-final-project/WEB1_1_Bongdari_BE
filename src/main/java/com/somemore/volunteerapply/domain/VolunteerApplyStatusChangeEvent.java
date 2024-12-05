package com.somemore.volunteerapply.domain;

import com.somemore.global.common.event.ServerEvent;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@SuperBuilder
public class VolunteerApplyStatusChangeEvent extends ServerEvent {

    private final UUID receiverId;
    private final Long volunteerApplyId;
    private final UUID centerId;
    private final Long recruitBoardId;
    private final ApplyStatus oldStatus;
    private final ApplyStatus newStatus;
}