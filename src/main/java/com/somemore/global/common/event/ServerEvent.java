package com.somemore.global.common.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public abstract class ServerEvent<T extends Enum<T>> {

    @JsonProperty("type")
    private final ServerEventType type;

    @JsonProperty("subType")
    private final T subType;

    @JsonProperty("createdAt")
    private final LocalDateTime createdAt;

    protected ServerEvent(
            @JsonProperty("type") ServerEventType type,
            @JsonProperty("subType") T subType,
            @JsonProperty("createdAt") LocalDateTime createdAt
    ) {
        this.type = type;
        this.subType = subType;
        this.createdAt = (createdAt == null) ? LocalDateTime.now() : createdAt;
    }
}