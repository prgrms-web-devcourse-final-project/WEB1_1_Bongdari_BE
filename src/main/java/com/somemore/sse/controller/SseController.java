package com.somemore.sse.controller;

import com.somemore.auth.annotation.CurrentUser;
import com.somemore.global.common.response.ApiResponse;
import com.somemore.sse.usecase.SseUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "SSE Subscribe API", description = "Server-Sent Events 구독 요청을 처리합니다.")
@RestController
@RequestMapping("/api/sse")
@RequiredArgsConstructor
public class SseController {

    private final SseUseCase sseUseCase;

    @Secured({"ROLE_VOLUNTEER", "ROLE_CENTER"})
    @GetMapping("/subscribe")
    public ApiResponse<String> subscribe(
            @CurrentUser UUID userId) {

        sseUseCase.subscribe(userId);
        return ApiResponse.ok("SSE 연결 성공");
    }
}
