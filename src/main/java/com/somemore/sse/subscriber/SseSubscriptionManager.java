package com.somemore.sse.subscriber;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

public interface SseSubscriptionManager {

    SseEmitter subscribe(UUID userId);
}
