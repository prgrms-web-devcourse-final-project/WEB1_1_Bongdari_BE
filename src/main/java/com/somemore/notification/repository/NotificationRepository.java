package com.somemore.notification.repository;

import com.somemore.notification.domain.Notification;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository {

    Notification save(Notification notification);
}
