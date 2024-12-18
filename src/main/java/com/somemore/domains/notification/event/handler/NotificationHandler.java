package com.somemore.domains.notification.event.handler;

import com.somemore.domains.notification.domain.Notification;

public interface NotificationHandler {

    void handle(Notification notification);
}
