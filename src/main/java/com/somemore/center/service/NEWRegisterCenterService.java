package com.somemore.center.service;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.repository.NEWCenterRepository;
import com.somemore.center.usecase.NEWRegisterCenterUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class NEWRegisterCenterService implements NEWRegisterCenterUseCase {

    private final NEWCenterRepository NEWCenterRepository;

    @Override
    public NEWCenter register(UUID userId) {
        NEWCenter center = NEWCenter.createDefault(userId);
        return NEWCenterRepository.save(center);
    }
}
