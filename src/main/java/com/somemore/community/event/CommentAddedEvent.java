package com.somemore.community.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.somemore.global.common.event.ServerEvent;
import com.somemore.global.common.event.ServerEventType;
import com.somemore.notification.domain.NotificationSubType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@SuperBuilder
public class CommentAddedEvent extends ServerEvent<NotificationSubType> {

    private final UUID volunteerId;
    private final Long communityBoardId;

    @JsonCreator
    public CommentAddedEvent(
            @JsonProperty(value = "volunteerId", required = true) UUID volunteerId,
            @JsonProperty(value = "communityBoardId", required = true) Long communityBoardId
    ) {
        super(ServerEventType.NOTIFICATION, NotificationSubType.COMMENT_ADDED, LocalDateTime.now());
        this.volunteerId = volunteerId;
        this.communityBoardId = communityBoardId;
    }
}
