package com.somemore.global.sse.sender;

import com.somemore.global.sse.domain.SseEvent;

public interface SseSender {
    <T> void send(SseEvent<T> sseEvent);
}
