package com.somemore.center.repository;

import com.somemore.center.domain.Center;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CenterRepository extends JpaRepository<Center, UUID> {

    boolean existsById(UUID id);

    default boolean doesNotExistById(UUID id) {
        return !existsById(id);
    }

    Optional<Center> findCenterById(UUID id);
}
