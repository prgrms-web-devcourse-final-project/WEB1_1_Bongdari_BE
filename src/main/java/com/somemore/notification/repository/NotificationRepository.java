package com.somemore.notification.repository;

import com.somemore.notification.domain.Notification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository {

    Notification save(Notification notification);

    List<Notification> findByReceiverIdAndUnread(UUID userId);
    List<Notification> findByReceiverIdAndRead(UUID userId);
}
