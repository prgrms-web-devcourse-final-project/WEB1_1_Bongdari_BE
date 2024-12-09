package com.somemore.global.common.event;

import com.somemore.notification.domain.NotificationSubType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum ServerEventType {
    NOTIFICATION(NotificationSubType.class),
    DOMAIN_EVENT(DomainEventSubType.class),
    ;

    private final Class<? extends Enum<?>> subtype;

    public static ServerEventType from(String value) {
        return Arrays.stream(ServerEventType.values())
                .filter(type -> type.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 이벤트 타입입니다: " + value));
    }
}
