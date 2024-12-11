package com.somemore.global.common.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public abstract class ServerEvent<T extends Enum<T>> {

    private final ServerEventType type;
    private final T subType;
    private final LocalDateTime createdAt;

    protected ServerEvent(
            @JsonProperty(value = "type", required = true) ServerEventType type,
            @JsonProperty(value = "subType", required = true) T subType,
            @JsonProperty(value = "createdAt", required = true) LocalDateTime createdAt
    ) {
        validate(type, subType);
        this.type = type;
        this.subType = subType;
        this.createdAt = (createdAt == null) ? LocalDateTime.now() : createdAt;
    }

    private void validate(ServerEventType type, T subType) {
        if (!type.getSubtype().isInstance(subType)) {
            throw new IllegalArgumentException(String.format(
                    "잘못된 서브타입: %s는 %s와 일치하지 않습니다.",
                    subType,
                    type.getSubtype()
            ));
        }
    }
}
