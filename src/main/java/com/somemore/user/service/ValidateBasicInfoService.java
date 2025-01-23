package com.somemore.user.service;

import com.somemore.user.usecase.UserQueryUseCase;
import com.somemore.user.usecase.ValidateBasicInfoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ValidateBasicInfoService implements ValidateBasicInfoUseCase {

    private final UserQueryUseCase userQueryUseCase;

    @Override
    public boolean isBasicInfoComplete(UUID userId) {
        return userQueryUseCase.getIsCustomizedByUserId(userId);
    }
}
