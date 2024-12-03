package com.somemore.global.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ApiResponse<T> {

    @Schema(description = "응답 상태 코드. 성공 시 200, 실패 시 오류 코드", example = "200")
    private int code;
    @Schema(description = "응답에 대한 메시지. 요청 성공/실패에 대한 설명", example = "요청 성공")
    private String message;
    @Schema(description = "API 요청 처리 결과로 반환되는 데이터", example = "{ name : 손모아 }")
    private T data;

    public static <T> ApiResponse<T> ok(int status, T data, String message) {
        return new ApiResponse<>(status, message, data);
    }

    public static ApiResponse<String> ok(String message) {
        return new ApiResponse<>(200, message, "");
    }

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
