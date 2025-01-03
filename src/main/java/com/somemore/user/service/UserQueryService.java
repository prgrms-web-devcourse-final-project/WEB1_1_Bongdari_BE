package com.somemore.user.service;

import com.somemore.global.exception.NoSuchElementException;
import com.somemore.user.domain.User;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.repository.user.UserRepository;
import com.somemore.user.repository.usercommonattribute.UserCommonAttributeRepository;
import com.somemore.user.usecase.UserQueryUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXIST_USER;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService implements UserQueryUseCase {

    private final UserRepository userRepository;
    private final UserCommonAttributeRepository userCommonAttributeRepository;

    @Override
    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(NOT_EXIST_USER));
    }

    @Override
    public UserCommonAttribute getCommonAttributeByUserId(UUID userId) {
        return userCommonAttributeRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException(NOT_EXIST_USER));
    }

    @Override
    public boolean getIsCustomizedByUserId(UUID userId) {
        return userCommonAttributeRepository.findIsCustomizedByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException(NOT_EXIST_USER));
    }

}
