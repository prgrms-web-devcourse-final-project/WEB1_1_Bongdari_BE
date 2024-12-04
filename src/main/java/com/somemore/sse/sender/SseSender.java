package com.somemore.sse.sender;

import com.somemore.sse.domain.Event;

public interface SseSender {
    <T> void send(Event<T> event);
}
