package com.somemore.domains.notification.controller;

import com.somemore.global.auth.annotation.CurrentUser;
import com.somemore.global.common.response.ApiResponse;
import com.somemore.domains.notification.dto.NotificationResponseDto;
import com.somemore.domains.notification.usecase.NotificationQueryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "Notification Query API", description = "알림 조회 API")
@RequiredArgsConstructor
@RequestMapping("/api/notification")
@RestController
public class NotificationQueryController {

    private final NotificationQueryUseCase notificationQueryUseCase;

    @Secured({"ROLE_VOLUNTEER", "ROLE_CENTER"})
    @Operation(summary = "읽지 않은 알림 조회", description = "읽지 않은 알림들을 조회합니다.")
    @GetMapping("/unread")
    public ApiResponse<List<NotificationResponseDto>> getUnreadNotifications(
            @CurrentUser UUID userId
    ) {
        return ApiResponse.ok(200,
                notificationQueryUseCase.getUnreadNotifications(userId),
                "읽지 않은 알림 조회 성공"
        );
    }

    @Secured({"ROLE_VOLUNTEER", "ROLE_CENTER"})
    @Operation(summary = "읽은 알림 조회", description = "읽은 알림들을 조회합니다.")
    @GetMapping("/read")
    public ApiResponse<List<NotificationResponseDto>> getReadNotifications(
            @CurrentUser UUID userId
    ) {

        return ApiResponse.ok(200,
                notificationQueryUseCase.getReadNotifications(userId),
                "읽은 알림 조회 성공"
        );
    }
}
