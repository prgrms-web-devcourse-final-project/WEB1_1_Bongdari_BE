package com.somemore.notification.event.handler;

import com.somemore.notification.domain.Notification;

public interface NotificationHandler {

    void handle(Notification notification);
}
