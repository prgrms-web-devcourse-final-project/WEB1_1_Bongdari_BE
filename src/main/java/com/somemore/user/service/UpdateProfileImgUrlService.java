package com.somemore.user.service;

import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.dto.request.UpdateProfileImgUrlRequestDto;
import com.somemore.user.usecase.UpdateProfileImgUrlUseCase;
import com.somemore.user.usecase.UserQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class UpdateProfileImgUrlService implements UpdateProfileImgUrlUseCase {

    private final UserQueryUseCase userQueryUseCase;

    @Override
    public void updateProfileImgUrl(UpdateProfileImgUrlRequestDto updateProfileImgUrlRequestDto) {
        UserCommonAttribute userCommonAttribute =
                userQueryUseCase.getCommonAttributeByUserId(updateProfileImgUrlRequestDto.userId());

        userCommonAttribute.updateImgUrl(updateProfileImgUrlRequestDto.profileImgUrl());
    }
}
