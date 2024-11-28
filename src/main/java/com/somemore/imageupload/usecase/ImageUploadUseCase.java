package com.somemore.imageupload.usecase;

import com.somemore.imageupload.dto.ImageUploadRequestDto;

public interface ImageUploadUseCase {
    String uploadImage(ImageUploadRequestDto requestDto);
}
