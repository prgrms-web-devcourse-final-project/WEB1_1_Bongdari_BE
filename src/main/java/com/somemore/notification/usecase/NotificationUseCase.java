package com.somemore.notification.usecase;

import com.somemore.notification.domain.NotificationType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

public interface NotificationUseCase {

    SseEmitter subscribe(UUID userId, String lastEventId);

    void send(UUID receiverId, String title, NotificationType notificationType, Long relatedId);
}
