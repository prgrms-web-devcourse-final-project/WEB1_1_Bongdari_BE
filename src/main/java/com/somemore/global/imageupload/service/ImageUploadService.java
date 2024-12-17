package com.somemore.global.imageupload.service;

import com.somemore.global.exception.ImageUploadException;
import com.somemore.global.imageupload.dto.ImageUploadRequestDto;
import com.somemore.global.imageupload.util.ImageUploadUtils;
import com.somemore.global.imageupload.validator.ImageUploadValidator;
import com.somemore.global.imageupload.usecase.ImageUploadUseCase;
import jakarta.annotation.PostConstruct;
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

    @Value("${default.image.url}")
    private String defaultImageUrl;

    public static String DEFAULT_IMAGE_URL;

    @PostConstruct
    private void init() {
        DEFAULT_IMAGE_URL = defaultImageUrl;
    }

    @Override
    public String uploadImage(ImageUploadRequestDto requestDto) {
        if (imageUploadValidator.isEmptyFile(requestDto.imageFile())) {
            return DEFAULT_IMAGE_URL;
        }

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
