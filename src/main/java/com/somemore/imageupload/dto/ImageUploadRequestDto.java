package com.somemore.imageupload.dto;

import org.springframework.web.multipart.MultipartFile;

public record ImageUploadRequestDto(
        MultipartFile imageFile
) {
}
