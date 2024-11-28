package com.somemore.imageupload.service;

import com.somemore.global.exception.ImageUploadException;
import com.somemore.imageupload.dto.ImageUploadRequestDto;
import com.somemore.imageupload.usecase.ImageUploadUseCase;
import com.somemore.imageupload.util.ImageUploadUtils;
import com.somemore.imageupload.validator.ImageUploadValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

import static com.somemore.global.exception.ExceptionMessage.UPLOAD_FAILED;

@RequiredArgsConstructor
@Service
public class ImageUploadService implements ImageUploadUseCase {

    private final S3Client s3Client;
    private final ImageUploadValidator imageUploadValidator;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.base-url}")
    private String baseUrl;

    @Override
    public String uploadImage(ImageUploadRequestDto requestDto) {
        imageUploadValidator.validateFileSize(requestDto.imageFile());
        imageUploadValidator.validateFileType(requestDto.imageFile());

        try {
            return uploadToS3(requestDto.imageFile());
        } catch (IOException e) {
            throw new ImageUploadException(UPLOAD_FAILED.getMessage());
        }
    }

    private String uploadToS3(MultipartFile file) throws IOException {
        String fileName = ImageUploadUtils.generateUniqueFileName(file.getOriginalFilename());

        PutObjectRequest request = createPutObjectRequest(file, fileName);

        s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return ImageUploadUtils.generateS3Url(baseUrl, fileName);
    }

    private PutObjectRequest createPutObjectRequest(MultipartFile file, String fileName) {
        return PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(file.getContentType())
                .build();
    }

}
