package com.somemore.center.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record CenterProfileImgUpdateRequestDto(
        @Schema(description = "파일 이름", example = "image.png")
        String fileName
) {
}
