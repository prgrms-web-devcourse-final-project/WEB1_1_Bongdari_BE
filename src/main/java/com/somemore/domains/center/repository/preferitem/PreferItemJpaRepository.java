package com.somemore.domains.center.repository.preferitem;

import com.somemore.domains.center.domain.PreferItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PreferItemJpaRepository extends JpaRepository<PreferItem, Long> {
    List<PreferItem> findByCenterId(UUID centerId);
}
