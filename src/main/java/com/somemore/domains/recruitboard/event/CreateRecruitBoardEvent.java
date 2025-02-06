package com.somemore.domains.recruitboard.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.somemore.domains.recruitboard.domain.RecruitBoard;
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

    public static CreateRecruitBoardEvent of(UUID centerId, RecruitBoard recruitBoard) {
        return CreateRecruitBoardEvent.builder()
                .type(ServerEventType.DOMAIN_EVENT)
                .subType(DomainEventSubType.CREATE_RECRUIT_BOARD)
                .centerId(centerId)
                .recruitBoardId(recruitBoard.getId())
                .build();
    }
}
