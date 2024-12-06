package com.somemore.sse.service;

import com.somemore.sse.domain.SseEvent;
import com.somemore.sse.domain.SseInitMessage;
import com.somemore.sse.sender.SseSender;
import com.somemore.sse.subscriber.SseSubscriptionManager;
import com.somemore.sse.usecase.SseUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService implements SseUseCase {

    private final SseSubscriptionManager subscriptionManager;
    private final SseSender sender;

    @Override
    public void subscribe(UUID userId) {
        subscriptionManager.subscribe(userId);
        sendInitMessage(userId);
    }

    @Override
    public <T> void send(SseEvent<T> event) {
        sender.send(event);
    }

    private void sendInitMessage(UUID userId) {
        sender.send(SseInitMessage.createInitEvent(userId));
    }
}
