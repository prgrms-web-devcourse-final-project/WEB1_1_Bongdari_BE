package com.somemore.center.repository.preferitem;

import com.somemore.center.domain.PreferItem;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferItemJpaRepository extends JpaRepository<PreferItem, Long> {

    List<PreferItem> findByCenterId(UUID centerId);
}
