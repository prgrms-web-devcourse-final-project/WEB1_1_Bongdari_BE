package com.somemore.domains.notification.event.subscriber;

import com.somemore.domains.notification.domain.Notification;
import com.somemore.domains.notification.event.converter.NotificationMessageConverter;
import com.somemore.domains.notification.event.handler.NotificationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisNotificationSubscriber implements MessageListener {

    private final NotificationHandler notificationHandler;
    private final NotificationMessageConverter messageConverter;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        Notification notification = messageConverter.from(
                new String(message.getBody())
        );

        notificationHandler.handle(notification);
    }
}
