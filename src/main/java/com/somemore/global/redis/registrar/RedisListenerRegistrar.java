package com.somemore.global.redis.registrar;

import com.somemore.interestcenter.event.subscriber.RedisCreateRecruitBoardSubscriber;
import com.somemore.notification.event.subscriber.RedisNotificationSubscriber;
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
    private final RedisCreateRecruitBoardSubscriber redisCreateRecruitBoardSubscriber;
    private final ChannelTopic notificationTopic;
    private final ChannelTopic domainEventTopic;

    @PostConstruct
    public void registerListeners() {
        registerNotificationListener();
    }

    private void registerNotificationListener() {
        container.addMessageListener(redisNotificationSubscriber, notificationTopic);
        container.addMessageListener(redisCreateRecruitBoardSubscriber, domainEventTopic);
        log.info("리스너가 토픽에 성공적으로 등록되었습니다.");
    }
}
