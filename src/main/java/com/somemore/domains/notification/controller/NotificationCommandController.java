package com.somemore.domains.notification.controller;

import com.somemore.domains.notification.dto.NotificationIdsRequestDto;
import com.somemore.domains.notification.usecase.NotificationCommandUseCase;
import com.somemore.global.auth.annotation.UserId;
import com.somemore.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Notification Command API", description = "알림 읽음 처리 API")
@RequiredArgsConstructor
@RequestMapping("/api/notification")
@RestController
public class NotificationCommandController {

    private final NotificationCommandUseCase notificationCommandUseCase;

    @Secured({"ROLE_VOLUNTEER", "ROLE_CENTER"})
    @Operation(summary = "알림(1개) 읽음 처리", description = "알림 1개를 읽음 처리합니다.")
    @PatchMapping("/read/{notificationId}")
    public ApiResponse<String> markSingleNotification(
            @UserId UUID userId,
            @PathVariable Long notificationId
    ) {
        notificationCommandUseCase.markSingleNotificationAsRead(userId, notificationId);
        return ApiResponse.ok("알림 1개 읽음 처리 성공");
    }

    @Secured({"ROLE_VOLUNTEER", "ROLE_CENTER"})
    @Operation(summary = "알림(N개) 읽음 처리", description = "알림 N개를 읽음 처리합니다.")
    @PostMapping("/read/multiple")
    public ApiResponse<String> markMultipleNotifications(
            @UserId UUID userId,
            @RequestBody NotificationIdsRequestDto notificationIds
    ) {
        notificationCommandUseCase.markMultipleNotificationsAsRead(userId, notificationIds);
        return ApiResponse.ok("알림 N개 읽음 처리 성공");
    }
}
