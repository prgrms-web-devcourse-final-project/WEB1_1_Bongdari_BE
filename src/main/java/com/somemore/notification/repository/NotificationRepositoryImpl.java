package com.somemore.notification.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.notification.domain.Notification;
import com.somemore.notification.domain.QNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaRepository notificationJpaRepository;
    private final JPAQueryFactory queryFactory;

    private static final QNotification notification = QNotification.notification;

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
    public List<Notification> findByReceiverIdAndUnread(UUID receiverId) {
        return queryFactory.selectFrom(notification)
                .where(eqReceiverId(receiverId),
                        isUnread())
                .fetch();
    }

    @Override
    public List<Notification> findByReceiverIdAndRead(UUID receiverId) {
        return queryFactory.selectFrom(notification)
                .where(eqReceiverId(receiverId),
                        isRead())
                .fetch();
    }

    private static BooleanExpression eqReceiverId(UUID userId) {
        return notification.receiverId.eq(userId);
    }

    private static BooleanExpression isUnread() {
        return notification.read.eq(false);
    }

    private static BooleanExpression isRead() {
        return notification.read.eq(true);
    }
}
