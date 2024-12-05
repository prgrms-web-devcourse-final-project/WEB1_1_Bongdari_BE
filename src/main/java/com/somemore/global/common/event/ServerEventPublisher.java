package com.somemore.global.common.event;

public interface ServerEventPublisher {

    <T extends Enum<T>> void publish(ServerEvent<T> event);
}
