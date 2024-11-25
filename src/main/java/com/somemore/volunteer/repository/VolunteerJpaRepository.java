package com.somemore.volunteer.repository;

import com.somemore.volunteer.domain.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VolunteerJpaRepository extends JpaRepository<Volunteer, Long> {
    Volunteer findById(UUID id);
    Optional<Volunteer> findByOauthId(String oauthId);
}
