package com.somemore.notification.repository;

import com.somemore.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {
}
