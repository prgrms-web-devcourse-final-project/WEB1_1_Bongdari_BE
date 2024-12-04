package com.somemore.notification.sse.usecase;

import com.somemore.notification.sse.domain.Event;

public interface SseSender {
    <T> void send(Event<T> event);
}
