package com.somemore.interestcenter.event;

import com.somemore.global.common.event.ServerEvent;
import com.somemore.notification.domain.NotificationSubType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@SuperBuilder
public class InterestCenterCreateRecruitBoardEvent extends ServerEvent<NotificationSubType> {
    private final UUID volunteerId;
    private final UUID centerId;
    private final Long recruitBoardId;
}
