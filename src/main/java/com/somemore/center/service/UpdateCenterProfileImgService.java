package com.somemore.center.service;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.dto.request.CenterProfileImgUpdateRequestDto;
import com.somemore.center.repository.NEWCenterRepository;
import com.somemore.center.usecase.UpdateCenterProfileImgUseCase;
import com.somemore.global.exception.BadRequestException;
import com.somemore.global.imageupload.usecase.ImageUploadUseCase;
import com.somemore.user.dto.request.UpdateProfileImgUrlRequestDto;
import com.somemore.user.usecase.UpdateProfileImgUrlUseCase;
import com.somemore.user.usecase.UserQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_CENTER;

@RequiredArgsConstructor
@Service
@Transactional
public class UpdateCenterProfileImgService implements UpdateCenterProfileImgUseCase {

    private final ImageUploadUseCase imageUploadUseCase;
    private final UserQueryUseCase userQueryUseCase;
    private final UpdateProfileImgUrlUseCase updateProfileImgUrlUseCase;
    private final NEWCenterRepository centerRepository;

    @Override
    public String updateCenterProfileImg(UUID centerId, CenterProfileImgUpdateRequestDto requestDto) {

        NEWCenter center = centerRepository.findById(centerId)
                .orElseThrow(() -> new BadRequestException(NOT_EXISTS_CENTER));

        String presignedUrl = imageUploadUseCase.getPresignedUrl(requestDto.fileName());

        String fileUrl = presignedUrl.split("\\?")[0];

        UpdateProfileImgUrlRequestDto updateProfileImgUrlRequestDto = new UpdateProfileImgUrlRequestDto(center.getUserId(), fileUrl);

        updateProfileImgUrlUseCase.updateProfileImgUrl(updateProfileImgUrlRequestDto);

        return presignedUrl;
    }
}
