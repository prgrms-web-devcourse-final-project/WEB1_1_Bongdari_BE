package com.somemore.global.imageupload.usecase;

import com.somemore.global.imageupload.dto.ImageUploadRequestDto;

public interface ImageUploadUseCase {
    String uploadImage(ImageUploadRequestDto requestDto);
    String getPresignedUrl(String filename);
}
