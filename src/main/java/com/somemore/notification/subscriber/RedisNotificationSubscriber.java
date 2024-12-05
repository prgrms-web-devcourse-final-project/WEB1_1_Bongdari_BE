package com.somemore.notification.subscriber;

import com.somemore.notification.converter.MessageConverter;
import com.somemore.notification.domain.Notification;
import com.somemore.notification.handler.NotificationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisNotificationSubscriber implements MessageListener {

    private final NotificationHandler notificationHandler;
    private final MessageConverter messageConverter;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        Notification notification = messageConverter.ToNotification(
                new String(message.getBody())
        );

        notificationHandler.handle(notification);
    }
}
