package com.somemore.notification.event.handler;

import com.somemore.notification.domain.Notification;
import com.somemore.notification.dto.NotificationResponseDto;
import com.somemore.notification.repository.NotificationRepository;
import com.somemore.sse.domain.SseEvent;
import com.somemore.sse.domain.SseEventType;
import com.somemore.sse.usecase.SseUseCase;
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
