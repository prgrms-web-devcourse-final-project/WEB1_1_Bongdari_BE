package com.somemore.center.controller;

import com.somemore.center.dto.response.CenterProfileResponseDto;
import com.somemore.center.usecase.query.CenterQueryUseCase;
import com.somemore.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/center")
@Tag(name = "Center Query API", description = "기관 관련 조회 API를 제공합니다.")
public class CenterQueryApiController {

    private final CenterQueryUseCase centerQueryUseCase;

    @Operation(summary = "기관 프로필 조회 API")
    @GetMapping("/profile/{centerId}")
    public ApiResponse<CenterProfileResponseDto> getCenterProfile(@PathVariable UUID centerId) {

        CenterProfileResponseDto responseDto = centerQueryUseCase.getCenterProfileByCenterId(centerId);

        return ApiResponse.ok(200, responseDto, "기관 프로필 조회 성공");
    }
}
