package com.somemore.recruitboard.event;

import com.somemore.global.common.event.DomainEventSubType;
import com.somemore.global.common.event.ServerEvent;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@SuperBuilder
public class CreateRecruitBoardEvent extends ServerEvent<DomainEventSubType> {
    private final UUID centerId;
    private final Long recruitBoardId;
}
