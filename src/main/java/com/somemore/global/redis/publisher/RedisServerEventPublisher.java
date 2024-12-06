package com.somemore.global.redis.publisher;

import com.somemore.global.common.event.ServerEvent;
import com.somemore.global.common.event.ServerEventPublisher;
import com.somemore.global.common.event.ServerEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisServerEventPublisher implements ServerEventPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Map<ServerEventType, ChannelTopic> eventTopics;

    public <T extends Enum<T>> void publish(ServerEvent<T> event) {
        ServerEventType type = event.getType();
        ChannelTopic topic = eventTopics.get(type);

        if (topic == null) {
            log.error("처리할 수 없는 이벤트 타입입니다: {}", type);
            throw new IllegalArgumentException("Unknown event type: " + type);
        }

        redisTemplate.convertAndSend(topic.getTopic(), event);
    }
}
