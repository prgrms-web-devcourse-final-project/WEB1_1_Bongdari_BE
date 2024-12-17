package com.somemore.global.imageupload.dto;

import org.springframework.web.multipart.MultipartFile;

public record ImageUploadRequestDto(
        MultipartFile imageFile
) {
}
