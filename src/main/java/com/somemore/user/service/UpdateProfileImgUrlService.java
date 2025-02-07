package com.somemore.user.service;

import com.somemore.global.imageupload.service.PresignedUrl;
import com.somemore.global.imageupload.usecase.ImageUploadUseCase;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.dto.request.ImgUrlRequestDto;
import com.somemore.user.usecase.UpdateProfileImgUrlUseCase;
import com.somemore.user.usecase.UserQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class UpdateProfileImgUrlService implements UpdateProfileImgUrlUseCase {

    private final ImageUploadUseCase imageUploadUseCase;
    private final UserQueryUseCase userQueryUseCase;

    @Override
    public String update(UUID userId, ImgUrlRequestDto dto) {
        PresignedUrl presignedUrl = imageUploadUseCase.getPresignedUrl(dto.fileName());

        UserCommonAttribute userCommonAttribute = userQueryUseCase.getCommonAttributeByUserId(userId);

        userCommonAttribute.updateImgUrl(presignedUrl.removeQueryString());

        return presignedUrl.value();
    }
}
