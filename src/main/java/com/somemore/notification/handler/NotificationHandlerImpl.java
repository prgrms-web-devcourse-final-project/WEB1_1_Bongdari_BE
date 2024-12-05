package com.somemore.notification.handler;

import com.somemore.notification.converter.MessageConverter;
import com.somemore.notification.domain.Notification;
import com.somemore.notification.domain.NotificationResponseDto;
import com.somemore.notification.repository.NotificationRepository;
import com.somemore.sse.domain.SseEvent;
import com.somemore.sse.domain.SseEventType;
import com.somemore.sse.sender.SseSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationHandlerImpl implements NotificationHandler {

    private final NotificationRepository notificationRepository;
    private final SseSender sseSender;

    @Override
    public void handle(String message) {
        Notification notification = MessageConverter.ToNotification(message);
        notificationRepository.save(notification);

        sseSender.send(createSseEvent(notification));
    }

    private static SseEvent<NotificationResponseDto> createSseEvent(Notification notification) {
        return SseEvent.<NotificationResponseDto>builder()
                .receiverId(notification.getReceiverId())
                .type(SseEventType.NOTIFICATION)
                .data(NotificationResponseDto.from(notification))
                .build();
    }
}