package com.somemore.domains.notification.repository;

import com.somemore.domains.notification.domain.Notification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationRepository {

    Notification save(Notification notification);

    Optional<Notification> findById(Long id);

    List<Notification> findAllByIds(List<Long> ids);

    List<Notification> findByReceiverIdAndUnread(UUID userId);
    List<Notification> findByReceiverIdAndRead(UUID userId);

    void deleteAllInBatch();
}
