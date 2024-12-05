package com.somemore.global.redis.registrar;

import com.somemore.notification.subscriber.RedisNotificationSubscriber;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisListenerRegistrar {

    private final RedisMessageListenerContainer container;
    private final RedisNotificationSubscriber redisNotificationSubscriber;
    private final ChannelTopic notificationTopic;

    @PostConstruct
    public void registerListeners() {
        registerNotificationListener();
    }

    private void registerNotificationListener() {
        container.addMessageListener(redisNotificationSubscriber, notificationTopic);
        log.info("Redis 알림 리스너가 '{}' 토픽에 성공적으로 등록되었습니다.", notificationTopic.getTopic());
    }
}