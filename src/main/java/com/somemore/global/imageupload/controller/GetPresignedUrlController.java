package com.somemore.global.imageupload.controller;

import com.somemore.global.auth.annotation.UserId;
import com.somemore.global.common.response.ApiResponse;
import com.somemore.global.imageupload.usecase.ImageUploadUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Presigned URL Query API", description = "이미지 업로드 URL 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class GetPresignedUrlController {

    private final ImageUploadUseCase imageUploadUseCase;

    @Secured("ROLE_VOLUNTEER, ROLE_CENTER")
    @PostMapping("/upload")
    @Operation(summary = "이미지 업로드 URL 조회", description = "이미지 업로드 URL을 조회합니다.")
    public ApiResponse<String> getImageUploadUrl(
            @UserId UUID userId,
            @RequestParam String fileName
    ) {
        // (이미지 업로드 유저 기록 방법, TIMESTAMP OR RANDOM UUID는 모두 적용)
        // 1. userId를 fileName과 함께 log 기반으로 저장, 이미지 업로드 유저 기록
        // 2. userId를 fileName과 함께 DB에 저장(별도의 테이블), 이미지 업로드 유저 기록
        // 3. userId를 fileName의 prefix or suffix로 설정, 이미지 업로드 유저 기록

        // (이미지 업로드 배드 케이스)
        // 1. S3에는 이미지가 있지만, DB에는 이미지 정보가 없는 경우
        // 클라이언트가 presignedUrl을 통해 S3에 이미지를 업로드했지만, 서버에 업로드 성공 여부를 알리지 않음.
        // 2. DB에는 이미지 URL이 있지만, S3에는 실제 파일이 없는 경우
        // 서버가 클라이언트에 presignedUrl을 제공한 직후, DB에 image_url을 저장했지만, 클라이언트가 실제로 업로드를 수행하지 않음.

        // 해결법
        // 업로드 완료 여부를 검증하기 위해 비동기적으로 S3와 DB를 동기화하는 별도 프로세스를 두는 방법
        // AWS S3 Event Notification + 서버 사이드 워커 (예: AWS Lambda, SQS, Kafka, 배치 작업)

        // 현재는 이미지 업로드를 성공적으로 마치고, 클라이언트가 별도의 컨트롤러를 호출해서 자신이 업로드한 이미지 URL을 잘 보내줄 것이라고 가정한 상태.
        return ApiResponse.ok(imageUploadUseCase.getPresignedUrl(fileName), "이미지 업로드 URL 발급 성공");
    }
}
