package com.somemore.domains.volunteer.repository.old;

import com.somemore.domains.volunteer.domain.Volunteer;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerJpaRepository extends JpaRepository<Volunteer, Long> {

    List<Volunteer> findAllByIdInAndDeletedFalse(List<UUID> ids);
    boolean existsByIdAndDeletedIsFalse(UUID id);
}
