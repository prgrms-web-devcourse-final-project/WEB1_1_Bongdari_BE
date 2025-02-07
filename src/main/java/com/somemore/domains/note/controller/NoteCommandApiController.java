package com.somemore.domains.note.controller;

import com.somemore.domains.note.dto.SendNoteToCenterRequestDto;
import com.somemore.domains.note.dto.SendNoteToVolunteerRequestDto;
import com.somemore.domains.note.usecase.SendNoteToCenterUseCase;
import com.somemore.domains.note.usecase.SendNoteToVolunteerUseCase;
import com.somemore.global.auth.annotation.RoleId;
import com.somemore.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Note Command API", description = "쪽지 송신 삭제 API")
@RequiredArgsConstructor
@RequestMapping("/api/note")
@RestController
public class NoteCommandApiController {

    private final SendNoteToCenterUseCase sendNoteToCenterUseCase;
    private final SendNoteToVolunteerUseCase sendNoteToVolunteerUseCase;

    @Secured("ROLE_VOLUNTEER")
    @Operation(summary = "봉사자 to 기관 쪽지 송신")
    @PostMapping("/volunteer-to-center")
    public ApiResponse<Long> sendNoteToCenter(
            @RoleId UUID volunteerId,
            @Valid @RequestBody SendNoteToCenterRequestDto requestDto
    ) {

        Long noteId = sendNoteToCenterUseCase.sendNoteToCenter(volunteerId, requestDto);

        return ApiResponse.ok(201, noteId, "쪽지 송신 성공");
    }

    @Secured("ROLE_CENTER")
    @Operation(summary = "기관 to 봉사자 쪽지 송신")
    @PostMapping("/center-to-volunteer")
    public ApiResponse<Long> sendNoteToCenter(
            @RoleId UUID centerId,
            @Valid @RequestBody SendNoteToVolunteerRequestDto requestDto
    ) {

        Long noteId = sendNoteToVolunteerUseCase.sendNoteToVolunteer(centerId, requestDto);

        return ApiResponse.ok(201, noteId, "쪽지 송신 성공");
    }
}
