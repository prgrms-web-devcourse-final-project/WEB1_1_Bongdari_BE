package com.somemore.domains.notification.repository;

import com.somemore.domains.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByReceiverId(UUID receiverId);
}
