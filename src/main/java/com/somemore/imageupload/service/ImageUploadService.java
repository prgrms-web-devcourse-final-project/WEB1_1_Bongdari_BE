package com.somemore.imageupload.service;

import com.somemore.global.exception.ImageUploadException;
import com.somemore.imageupload.dto.ImageUploadRequestDto;
import com.somemore.imageupload.usecase.ImageUploadUseCase;
import com.somemore.imageupload.util.ImageUploadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

import static com.somemore.global.exception.ExceptionMessage.UPLOAD_FAILED;

@RequiredArgsConstructor
@Service
public class ImageUploadService implements ImageUploadUseCase {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.base-url}")
    private String baseUrl;

    @Override
    public String uploadImage(ImageUploadRequestDto requestDto) {

        String fileName = ImageUploadUtils.generateUniqueFileName(requestDto.imageFile().getOriginalFilename());

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(requestDto.imageFile().getContentType())
                .build();

        try {
            s3Client.putObject(request, RequestBody.fromInputStream(
                    requestDto.imageFile().getInputStream(),
                    requestDto.imageFile().getSize()
            ));

            return ImageUploadUtils.generateS3Url(baseUrl, fileName);
        } catch (IOException e) {
            throw new ImageUploadException(UPLOAD_FAILED.getMessage());
        }
    }

}
