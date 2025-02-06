package com.somemore.center.repository.preferitem;

import com.somemore.center.domain.PreferItem;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PreferItemRepositoryImpl implements PreferItemRepository {

    private final PreferItemJpaRepository preferItemJpaRepository;

    @Override
    public void save(PreferItem preferItem) {
        preferItemJpaRepository.save(preferItem);
    }

    @Override
    public void deleteById(Long preferItemId) {
        preferItemJpaRepository.deleteById(preferItemId);
    }

    @Override
    public Optional<PreferItem> findById(Long preferItemId) {
        return preferItemJpaRepository.findById(preferItemId);
    }

    @Override
    public List<PreferItem> findByCenterId(UUID centerId) {
        return preferItemJpaRepository.findByCenterId(centerId);
    }

}
