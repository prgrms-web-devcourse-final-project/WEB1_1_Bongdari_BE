package com.somemore.notification.repository.notification;

import com.somemore.notification.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository{

    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public Notification save(Notification notification) {
        return notificationJpaRepository.save(notification);
    }
}
