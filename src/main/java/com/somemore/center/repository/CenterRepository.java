package com.somemore.center.repository;

import com.somemore.center.domain.Center;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CenterRepository {
    Center save(Center center);
    boolean existsById(UUID id);
    default boolean doesNotExistById(UUID id) {
        return !existsById(id);
    }
    Optional<Center> findCenterById(UUID id);
    String findNameById(UUID id);
    void deleteAllInBatch();
}
