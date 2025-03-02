package com.somemore.domains.interestcenter.event.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.somemore.domains.notification.domain.NotificationSubType;
import com.somemore.domains.recruitboard.event.CreateRecruitBoardEvent;
import com.somemore.global.common.event.ServerEvent;
import com.somemore.global.common.event.ServerEventType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@SuperBuilder
public class InterestCenterCreateRecruitBoardEvent extends ServerEvent<NotificationSubType> {
    private final UUID volunteerId;
    private final UUID centerId;
    private final Long recruitBoardId;

    @JsonCreator
    public InterestCenterCreateRecruitBoardEvent(
            @JsonProperty(value = "volunteerId", required = true) UUID volunteerId,
            @JsonProperty(value = "centerId", required = true) UUID centerId,
            @JsonProperty(value = "recruitBoardId", required = true) Long recruitBoardId
    ) {
        super(ServerEventType.NOTIFICATION, NotificationSubType.INTEREST_CENTER_CREATE_RECRUIT_BOARD, LocalDateTime.now());
        this.volunteerId = volunteerId;
        this.centerId = centerId;
        this.recruitBoardId = recruitBoardId;
    }

    public static InterestCenterCreateRecruitBoardEvent of(CreateRecruitBoardEvent createRecruitBoardEvent, UUID volunteerId, UUID centerId) {
        return InterestCenterCreateRecruitBoardEvent.builder()
                .type(ServerEventType.NOTIFICATION)
                .subType(NotificationSubType.INTEREST_CENTER_CREATE_RECRUIT_BOARD)
                .volunteerId(volunteerId)
                .centerId(centerId)
                .recruitBoardId(createRecruitBoardEvent.getRecruitBoardId())
                .build();
    }
}
