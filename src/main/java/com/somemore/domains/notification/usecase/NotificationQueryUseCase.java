package com.somemore.domains.notification.usecase;

import com.somemore.domains.notification.dto.NotificationResponseDto;

import java.util.List;
import java.util.UUID;

public interface NotificationQueryUseCase {

    List<NotificationResponseDto> getUnreadNotifications(UUID userId);

    List<NotificationResponseDto> getReadNotifications(UUID userId);
}
