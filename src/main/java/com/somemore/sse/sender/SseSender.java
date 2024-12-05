package com.somemore.sse.sender;

import com.somemore.sse.domain.SseEvent;

public interface SseSender {
    <T> void send(SseEvent<T> sseEvent);
}
