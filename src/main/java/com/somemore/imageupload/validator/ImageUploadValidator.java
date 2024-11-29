package com.somemore.imageupload.validator;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadValidator {

    void validateFileSize(MultipartFile file);
    void validateFileType(MultipartFile file);
    boolean isEmptyFile(MultipartFile file);
}
