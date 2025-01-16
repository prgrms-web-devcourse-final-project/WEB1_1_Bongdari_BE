package com.somemore.domains.center.controller;

import com.somemore.domains.center.dto.request.PreferItemCreateRequestDto;
import com.somemore.domains.center.dto.response.PreferItemCreateResponseDto;
import com.somemore.domains.center.usecase.command.CreatePreferItemUseCase;
import com.somemore.domains.center.usecase.command.DeletePreferItemUseCase;
import com.somemore.global.auth.annotation.CurrentUser;
import com.somemore.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/preferItem")
@Tag(name = "PreferItem Command API", description = "선호 물품 등록, 삭제 기능을 제공합니다")
public class PreferItemCommandApiController {

    private final CreatePreferItemUseCase createPreferItemUseCase;
    private final DeletePreferItemUseCase deletePreferItemUseCase;

    @Secured("ROLE_CENTER")
    @Operation(summary = "기관 선호물품 등록 API")
    @PostMapping()
    public ApiResponse<PreferItemCreateResponseDto> registerPreferItem(
            @Valid @RequestBody PreferItemCreateRequestDto requestDto,
            @CurrentUser UUID userId) {

        PreferItemCreateResponseDto responseDto = createPreferItemUseCase.createPreferItem(userId,
                requestDto);

        return ApiResponse.ok(200, responseDto, "관심 기관 등록 성공");
    }

    @Secured("ROLE_CENTER")
    @Operation(summary = "기관 선호물품 삭제 API")
    @DeleteMapping("/{preferItemId}")
    public ApiResponse<String> deletePreferItem(@CurrentUser UUID centerId, @PathVariable Long preferItemId) {

        deletePreferItemUseCase.deletePreferItem(centerId, preferItemId);

        return ApiResponse.ok("선호 물품 삭제 성공");
    }
}
