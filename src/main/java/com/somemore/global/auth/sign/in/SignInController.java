package com.somemore.global.auth.sign.in;

import com.somemore.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping()
@Tag(name = "SignIn API", description = "유저 로그인")
public class SignInController {

    /**
     * ID 및 PW 기반 로그인 엔드포인트 (Swagger 문서화를 위한 더미 엔드포인트)
     * <p>
     * 이 엔드포인트는 실제로 동작하지 않으며, Spring Security의 필터 체인에 의해 요청이 처리됩니다.
     * 사용자가 ID/PW로 로그인을 시도하면, 해당 요청은 Spring Security 필터 체인에서 가로채어 인증 절차를 수행하며,
     * 이 엔드포인트로 전달되지 않습니다.
     * <p>
     * 따라서 이 엔드포인트는 실제 인증 로직과는 무관하며, Swagger API 문서화를 위해 정의된 것입니다.
     * <p>
     * 실제 인증 절차는 Spring Security의 필터에서 처리됩니다.
     */
    @PostMapping("/api/sign-in/id-pw")
    public ApiResponse<String> signIn(
            @RequestParam SignInRequestDto signInRequestDto) {
        return ApiResponse.ok("로그인되었습니다.");
    }

    /**
     * OAuth2 소셜 로그인 엔드포인트 (Swagger 문서화를 위한 더미 엔드포인트)
     * <p>
     * 이 엔드포인트는 실제로 동작하지 않으며, Spring Security가 제공하는 OAuth2 클라이언트 기능에 의해 처리됩니다.
     * 클라이언트가 특정 OAuth 제공자(Naver, Kakao 등)로 로그인 요청을 보내면,
     * 요청은 Spring Security의 OAuth2 필터 체인에 의해 가로채어 처리되며, 이 엔드포인트로 전달되지 않습니다.
     * <p>
     * 따라서 이 엔드포인트는 실제 소셜 로그인 처리를 위한 진입점이 아니라,
     * Swagger API 문서화를 위해 정의된 것입니다.
     * <p>
     * 실제 OAuth2 로그인 절차는 Spring Security가 처리합니다.
     */
    @GetMapping("oauth2/authorization/{oauthProvider}")
    public ApiResponse<String> signIn(
            @Parameter(name = "oauthProvider", description = "OAuth 제공자 선택", example = "naver", required = true, schema = @Schema(allowableValues = {"naver, kakao"}))
            @PathVariable("oauthProvider") String oauthProvider) {

        return ApiResponse.ok("로그인되었습니다.");
    }
}
