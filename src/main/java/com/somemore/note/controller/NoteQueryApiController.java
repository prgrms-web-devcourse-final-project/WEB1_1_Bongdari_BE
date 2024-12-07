package com.somemore.note.controller;

import com.somemore.auth.annotation.CurrentUser;
import com.somemore.global.common.response.ApiResponse;
import com.somemore.note.repository.mapper.NoteReceiverViewForCenter;
import com.somemore.note.usecase.NoteQueryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Note Query API", description = "쪽지 조회 API")
@RequiredArgsConstructor
@RequestMapping("/api/note")
@RestController
public class NoteQueryApiController {

    private final NoteQueryUseCase noteQueryUseCase;

    @Secured("ROLE_CENTER")
    @Operation(summary = "기관의 자신에게 온 쪽지 조회")
    @GetMapping("/center")
    public ApiResponse<Page<NoteReceiverViewForCenter>> getNotesByCenterId(@CurrentUser UUID centerId, Pageable pageable) {

        Page<NoteReceiverViewForCenter> response = noteQueryUseCase.getNotesForCenter(centerId, pageable);

        return ApiResponse.ok(200, response, "내 쪽지 조회 성공");
    }
}
