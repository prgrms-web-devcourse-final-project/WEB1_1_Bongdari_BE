package com.somemore.center.repository;

import com.somemore.center.domain.Center;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class CenterRepositoryImpl implements CenterRepository {

    private final CenterJpaRepository centerJpaRepository;

    @Override
    public Center save(Center center) {
        return centerJpaRepository.save(center);
    }

    @Override
    public boolean existsById(UUID id) {
        return centerJpaRepository.existsById(id);
    }

    @Override
    public Optional<Center> findCenterById(UUID id) {
        return centerJpaRepository.findCenterById(id);
    }

    @Override
    public void deleteAllInBatch() {
        centerJpaRepository.deleteAllInBatch();
    }
}
