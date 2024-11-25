package com.somemore.center.repository;

import com.somemore.center.domain.PreferItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PreferItemRepository extends JpaRepository<PreferItem, Long> {

    List<PreferItem> findByCenterId(UUID centerId);

}
