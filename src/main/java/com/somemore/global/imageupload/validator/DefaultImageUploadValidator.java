package com.somemore.global.imageupload.validator;

import com.somemore.global.exception.ImageUploadException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import static com.somemore.global.exception.ExceptionMessage.FILE_SIZE_EXCEEDED;
import static com.somemore.global.exception.ExceptionMessage.INVALID_FILE_TYPE;

@Component
public class DefaultImageUploadValidator implements ImageUploadValidator {

    private static final long MAX_FILE_SIZE = 8L * 1024 * 1024; // 8MB

    public void validateFileSize(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ImageUploadException(FILE_SIZE_EXCEEDED.getMessage());
        }
    }

    public void validateFileType(MultipartFile file) {
        String contentType = file.getContentType();
        if (!isAllowedImageType(contentType)) {
            throw new ImageUploadException(INVALID_FILE_TYPE.getMessage());
        }
    }

    @Override
    public boolean isEmptyFileName(String fileName) {
        return fileName == null || fileName.isEmpty();
    }

    @Override
    public boolean isEmptyFile(MultipartFile file) {
        return file == null || file.isEmpty();
    }

    private boolean isAllowedImageType(String contentType) {
        return contentType != null && (
                contentType.equals("image/jpeg") ||
                        contentType.equals("image/png") ||
                        contentType.equals("image/gif") ||
                        contentType.equals("image/webp")
        );
    }
}
