package com.somemore.center.service;

import com.somemore.center.domain.Center_NEW;
import com.somemore.center.repository.CenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class registerCenterService implements registerCenterUseCase {

    private final CenterRepository centerRepository;

    @Override
    public Center_NEW register(UUID userId) {
        Center_NEW center = Center_NEW.createDefault(userId);
        return centerRepository.save(center);
    }
}
