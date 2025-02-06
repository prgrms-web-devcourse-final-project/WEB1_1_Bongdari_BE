package com.somemore.center.repository.preferitem;

import com.somemore.center.domain.PreferItem;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferItemRepository {

    void save(PreferItem preferItem);

    void deleteById(Long preferItemId);

    Optional<PreferItem> findById(Long preferItemId);

    List<PreferItem> findByCenterId(UUID centerId);
}
