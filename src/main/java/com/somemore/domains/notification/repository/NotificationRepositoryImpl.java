package com.somemore.domains.notification.repository;

import com.somemore.domains.notification.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public Notification save(Notification notification) {
        return notificationJpaRepository.save(notification);
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notificationJpaRepository.findById(id);
    }

    @Override
    public List<Notification> findAllByIds(List<Long> ids) {
        return notificationJpaRepository.findAllById(ids);
    }

    @Override
    public List<Notification> findAllByUserId(UUID receiverId) {
        return notificationJpaRepository.findAllByReceiverId(receiverId);
    }

    @Override
    public void deleteAllInBatch() {
        notificationJpaRepository.deleteAllInBatch();
    }
}
