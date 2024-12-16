package com.somemore.global.sse.controller;

import com.somemore.global.auth.annotation.CurrentUser;
import com.somemore.global.sse.usecase.SseUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@Tag(name = "SSE Subscribe API", description = "Server-Sent Events 구독 요청을 처리합니다.")
@RestController
@RequestMapping("/api/sse")
@RequiredArgsConstructor
public class SseController {

    private final SseUseCase sseUseCase;

    @Secured({"ROLE_VOLUNTEER", "ROLE_CENTER"})
    @GetMapping("/subscribe")
    public SseEmitter subscribe(
            @CurrentUser UUID userId) {

        return sseUseCase.subscribe(userId);
    }
}
