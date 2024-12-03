package com.somemore.notification.service;

import com.somemore.notification.domain.Notification;
import com.somemore.notification.domain.NotificationType;
import com.somemore.notification.dto.NotificationResponseDto;
import com.somemore.notification.repository.emitter.EmitterRepository;
import com.somemore.notification.repository.eventcache.EventCacheRepository;
import com.somemore.notification.repository.notification.NotificationRepository;
import com.somemore.notification.usecase.NotificationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class NotificationService implements NotificationUseCase {

    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;
    private final EventCacheRepository eventCacheRepository;
    private static final Long timeoutMillis = 600_000L;

    public SseEmitter subscribe(UUID userId,
                                String lastEventId) {
        String emitterId = createUniqueId(userId);
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(timeoutMillis));
        setupEmitterLifeCycleCallbacks(emitter, emitterId);

        sendNotification(emitter, emitterId, emitterId, String.format("EventStream init. [userId=%s]", userId));
        processLostData(userId, lastEventId, emitterId, emitter);

        return emitter;
    }

    public void send(UUID receiverId,
                     String title,
                     NotificationType type,
                     Long relatedId) {
        Notification notification = notificationRepository.save(createNotification(receiverId, title, type, relatedId));

        String eventId = createUniqueId(receiverId);
        emitterRepository.findAllByUserId(receiverId).forEach(
                (id, emitter) -> {
                    eventCacheRepository.save(id, notification);
                    sendNotification(emitter, eventId, id, NotificationResponseDto.from(notification));
                }
        );
    }

    private String createUniqueId(UUID id) {
        return id.toString() + "_" + System.currentTimeMillis();
    }

    private void setupEmitterLifeCycleCallbacks(SseEmitter emitter, String emitterId) {
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
    }

    private void sendNotification(SseEmitter emitter,
                                  String eventId,
                                  String emitterId,
                                  Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));
        } catch (IOException e) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private Notification createNotification(UUID receiverId,
                                            String title,
                                            NotificationType type,
                                            Long relatedId) {
        return Notification.builder()
                .receiverId(receiverId)
                .title(title)
                .type(type)
                .relatedId(relatedId)
                .build();
    }

    private void processLostData(UUID userId, String lastEventId, String emitterId, SseEmitter emitter) {
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, userId, emitterId, emitter);
        }
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId,
                              UUID userId,
                              String emitterId,
                              SseEmitter emitter) {
        Map<String, Object> eventCaches = eventCacheRepository.findAllByUserId(userId);
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

}
