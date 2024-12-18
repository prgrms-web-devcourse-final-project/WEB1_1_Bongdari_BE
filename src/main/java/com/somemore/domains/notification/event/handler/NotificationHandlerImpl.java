package com.somemore.domains.notification.event.handler;

import com.somemore.domains.notification.domain.Notification;
import com.somemore.domains.notification.dto.NotificationResponseDto;
import com.somemore.domains.notification.repository.NotificationRepository;
import com.somemore.global.sse.domain.SseEvent;
import com.somemore.global.sse.domain.SseEventType;
import com.somemore.global.sse.usecase.SseUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class NotificationHandlerImpl implements NotificationHandler {

    private final NotificationRepository notificationRepository;
    private final SseUseCase sseUseCase;

    @Override
    public void handle(Notification notification) {
        notificationRepository.save(notification);

        sseUseCase.send(createSseEvent(notification));
    }

    private static SseEvent<NotificationResponseDto> createSseEvent(Notification notification) {
        return SseEvent.<NotificationResponseDto>builder()
                .receiverId(notification.getReceiverId())
                .type(SseEventType.NOTIFICATION)
                .data(NotificationResponseDto.from(notification))
                .build();
    }
}
