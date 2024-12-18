package com.somemore.domains.recruitboard.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.somemore.global.common.event.DomainEventSubType;
import com.somemore.global.common.event.ServerEvent;
import com.somemore.global.common.event.ServerEventType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@SuperBuilder
public class CreateRecruitBoardEvent extends ServerEvent<DomainEventSubType> {
    private final UUID centerId;
    private final Long recruitBoardId;

    @JsonCreator
    public CreateRecruitBoardEvent(
            @JsonProperty(value = "centerId", required = true) UUID centerId,
            @JsonProperty(value = "recruitBoardId", required = true) Long recruitBoardId
    ) {
        super(ServerEventType.DOMAIN_EVENT, DomainEventSubType.CREATE_RECRUIT_BOARD, LocalDateTime.now());
        this.centerId = centerId;
        this.recruitBoardId = recruitBoardId;
    }
}
