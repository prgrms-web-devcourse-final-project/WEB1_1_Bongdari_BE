package com.somemore.notification.handler;

import com.somemore.notification.domain.Notification;

public interface NotificationHandler {

    void handle(Notification notification);
}
