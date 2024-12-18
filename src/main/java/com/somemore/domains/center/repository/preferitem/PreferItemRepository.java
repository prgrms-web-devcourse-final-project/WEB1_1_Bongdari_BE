package com.somemore.domains.center.repository.preferitem;

import com.somemore.domains.center.domain.PreferItem;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PreferItemRepository {
    void save(PreferItem preferItem);
    void deleteById(Long preferItemId);
    Optional<PreferItem> findById(Long preferItemId);
    List<PreferItem> findByCenterId(UUID centerId);
}
