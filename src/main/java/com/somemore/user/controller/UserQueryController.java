package com.somemore.user.controller;

import com.somemore.global.common.response.ApiResponse;
import com.somemore.user.usecase.UserQueryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User Query API", description = "유저 조회 관련 API")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserQueryController {

    private final UserQueryUseCase userQueryUseCase;

    @GetMapping("/exists/")
    @Operation(summary = "아이디 중복 확인", description = "입력한 아이디가 중복되었는지 확인합니다.")
    public ApiResponse<Boolean> checkUserExists(
            @RequestParam String accountId
    ) {
        boolean isAccountIdDuplicate = userQueryUseCase.isDuplicateAccountId(accountId);
        return ApiResponse.ok(isAccountIdDuplicate, "중복 조회 완료");
    }
}
