package com.somemore.global.common.event;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public abstract class ServerEvent {

    private final ServerEventType type;

    @Builder.Default
    private final LocalDateTime createdAt = LocalDateTime.now();
}
