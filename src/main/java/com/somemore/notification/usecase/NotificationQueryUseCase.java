package com.somemore.notification.usecase;

import com.somemore.notification.dto.NotificationResponseDto;

import java.util.List;
import java.util.UUID;

public interface NotificationQueryUseCase {

    List<NotificationResponseDto> getUnreadNotifications(UUID userId);
    List<NotificationResponseDto> getReadNotifications(UUID userId);
}
