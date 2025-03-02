package com.somemore.user.controller;

import com.somemore.global.auth.annotation.UserId;
import com.somemore.global.common.response.ApiResponse;
import com.somemore.user.usecase.UserQueryUseCase;
import com.somemore.user.usecase.ValidateBasicInfoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "User Query API", description = "유저 조회 관련 API")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserQueryController {

    private final UserQueryUseCase userQueryUseCase;
    private final ValidateBasicInfoUseCase validateBasicInfoUseCase;

    @GetMapping("/exists")
    @Operation(summary = "아이디 중복 확인", description = "입력한 아이디가 중복되었는지 확인합니다.")
    public ApiResponse<Boolean> checkUserExists(
            @RequestParam String accountId
    ) {
        boolean isAccountIdDuplicate = userQueryUseCase.isDuplicateAccountId(accountId);
        return ApiResponse.ok(isAccountIdDuplicate, "중복 조회 완료");
    }

    @GetMapping("/check/basic-info")
    @Operation(summary = "유저 기본 정보 입력 상태 확인", description = "현재 유저의 필수 입력 정보가 모두 완료되었는지 확인합니다.")
    public ApiResponse<Boolean> checkBasicInfo(
            @UserId UUID userId
    ) {
        boolean isBasicInfoComplete = validateBasicInfoUseCase.isBasicInfoComplete(userId);
        return ApiResponse.ok(isBasicInfoComplete, "필수 입력 정보 설정 조회 완료");
    }
}
