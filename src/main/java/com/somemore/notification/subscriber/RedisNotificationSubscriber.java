package com.somemore.notification.subscriber;

import com.somemore.notification.handler.NotificationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisNotificationSubscriber implements MessageListener {

    private final NotificationHandler notificationHandler;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String data = new String(message.getBody());

        notificationHandler.handle(data);
    }
}
