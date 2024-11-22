package com.somemore.center.repository;

import com.somemore.center.domain.PreferItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PreferItemRepository extends JpaRepository<PreferItem, Long> {

    @Query("SELECT p FROM PreferItem p WHERE p.centerId = :centerId")
    List<PreferItem> findByCenterId(@Param("centerId") UUID centerId);

}