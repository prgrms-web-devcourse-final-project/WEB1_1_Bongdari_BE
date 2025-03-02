package com.somemore.domains.note.controller;

import com.somemore.domains.note.repository.mapper.NoteDetailViewForCenter;
import com.somemore.domains.note.repository.mapper.NoteDetailViewForVolunteer;
import com.somemore.domains.note.repository.mapper.NoteReceiverViewForCenter;
import com.somemore.domains.note.repository.mapper.NoteReceiverViewForVolunteer;
import com.somemore.domains.note.usecase.NoteQueryUseCase;
import com.somemore.global.auth.annotation.RoleId;
import com.somemore.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Note Query API", description = "쪽지 조회 API")
@RequiredArgsConstructor
@RequestMapping("/api/note")
@RestController
public class NoteQueryApiController {

    private final NoteQueryUseCase noteQueryUseCase;

    @Secured("ROLE_CENTER")
    @Operation(summary = "기관의 자신에게 온 쪽지 조회")
    @GetMapping("/center")
    public ApiResponse<Page<NoteReceiverViewForCenter>> getNotesByCenterId(
            @RoleId UUID centerId,
            Pageable pageable) {

        Page<NoteReceiverViewForCenter> response = noteQueryUseCase.getNotesForCenter(centerId,
                pageable);

        return ApiResponse.ok(200, response, "내 쪽지 조회 성공");
    }

    @Secured("ROLE_VOLUNTEER")
    @Operation(summary = "봉사자의 자신에게 온 쪽지 조회")
    @GetMapping("/volunteer")
    public ApiResponse<Page<NoteReceiverViewForVolunteer>> getNotesByVolunteerId(
            @RoleId UUID volunteerId,
            Pageable pageable
    ) {

        Page<NoteReceiverViewForVolunteer> response = noteQueryUseCase.getNotesForVolunteer(
                volunteerId, pageable);

        return ApiResponse.ok(200, response, "내 쪽지 조회 성공");
    }

    @Secured("ROLE_CENTER")
    @Operation(summary = "기관의 자신에게 온 쪽지 상세 조회")
    @GetMapping("/center/{noteId}")
    public ApiResponse<NoteDetailViewForCenter> getNoteDetailForCenter(@PathVariable Long noteId) {

        NoteDetailViewForCenter response = noteQueryUseCase.getNoteDetailForCenter(noteId);

        return ApiResponse.ok(200, response, "쪽지 상세 조회 성공");
    }

    @Secured("ROLE_VOLUNTEER")
    @Operation(summary = "봉사자의 자신에게 온 쪽지 상세 조회")
    @GetMapping("/volunteer/{noteId}")
    public ApiResponse<NoteDetailViewForVolunteer> getNoteDetailForVolunteer(
            @PathVariable Long noteId) {

        NoteDetailViewForVolunteer response = noteQueryUseCase.getNoteDetailForVolunteer(noteId);

        return ApiResponse.ok(200, response, "쪽지 상세 조회 성공");
    }

}
