package com.somemore.domains.volunteer.repository;

import com.somemore.domains.volunteer.domain.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("oldVolunteerJpaRepository")
public interface VolunteerJpaRepository extends JpaRepository<Volunteer, Long> {

    List<Volunteer> findAllByIdInAndDeletedFalse(List<UUID> ids);

    boolean existsByIdAndDeletedIsFalse(UUID id);
}
