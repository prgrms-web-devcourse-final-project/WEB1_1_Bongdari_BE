package com.somemore.global.imageupload.usecase;

import com.somemore.global.imageupload.dto.ImageUploadRequestDto;
import com.somemore.global.imageupload.service.PresignedUrl;

public interface ImageUploadUseCase {
    String uploadImage(ImageUploadRequestDto requestDto);

    PresignedUrl getPresignedUrl(String filename);
}
