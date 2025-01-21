package com.somemore.center.service;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.repository.NEWCenterRepository;
import com.somemore.center.usecase.NEWCenterQueryUseCase;
import com.somemore.global.exception.ExceptionMessage;
import com.somemore.global.exception.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NEWCenterQueryService implements NEWCenterQueryUseCase {

    private final NEWCenterRepository centerRepository;

    @Override
    public NEWCenter getByUserId(UUID userId) {
        return centerRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException(ExceptionMessage.NOT_EXISTS_CENTER));
    }

    @Override
    public UUID getIdByUserId(UUID userId) {
        return getByUserId(userId).getId();
    }
}
