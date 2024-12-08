package com.somemore.center.repository.center;

import com.somemore.center.domain.Center;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CenterJpaRepository extends JpaRepository<Center, Long> {
    boolean existsById(UUID id);
    Optional<Center> findCenterById(UUID id);
    Optional<Center> findByName(String name);
    boolean existsByIdAndDeletedIsFalse(UUID id);
}
