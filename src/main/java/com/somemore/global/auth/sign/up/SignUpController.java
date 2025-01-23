package com.somemore.global.auth.sign.up;

import com.somemore.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "SignUp API", description = "유저 회원가입")
public class SignUpController {

    private final SignUpUseCase signUpUseCase;

    @PostMapping("/sign-up")
    public ApiResponse<String> signUp(
            @RequestBody SignUpRequestDto signUpRequestDto
    ) {
        signUpUseCase.signUpLocalUser(signUpRequestDto);

        return ApiResponse.ok("회원가입 되었습니다");
    }
}
