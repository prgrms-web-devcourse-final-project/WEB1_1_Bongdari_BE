package com.somemore.note.controller;

import com.somemore.auth.annotation.CurrentUser;
import com.somemore.global.common.response.ApiResponse;
import com.somemore.note.dto.SendNoteToCenterRequestDto;
import com.somemore.note.usecase.SendNoteToCenterUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Note Command API", description = "쪽지 송신 삭제 API")
@RequiredArgsConstructor
@RequestMapping("/api/note")
@RestController
public class NoteCommandApiController {

    private final SendNoteToCenterUseCase sendNoteToCenterUseCase;

    @Secured("ROLE_VOLUNTEER")
    @Operation(summary = "봉사자 to 기관 쪽지 송신")
    @PostMapping(value = "/volunteer-to-center")
    public ApiResponse<Long> sendNoteToCenter(@CurrentUser UUID userId, @Valid @RequestBody SendNoteToCenterRequestDto requestDto) {

        Long noteId = sendNoteToCenterUseCase.sendNoteToCenter(userId, requestDto);

        return ApiResponse.ok(201, noteId, "쪽지 송신 성공");
    }
}
