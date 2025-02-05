package com.somemore.domains.community.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.somemore.domains.notification.domain.NotificationSubType;
import com.somemore.global.common.event.ServerEvent;
import com.somemore.global.common.event.ServerEventType;
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

    public static CommentAddedEvent of(UUID targetUserId, Long communityBoardId) {
        return CommentAddedEvent.builder()
                .type(ServerEventType.NOTIFICATION)
                .subType(NotificationSubType.COMMENT_ADDED)
                .volunteerId(targetUserId)
                .communityBoardId(communityBoardId)
                .build();
    }
}
