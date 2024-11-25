package com.somemore.volunteer.repository;

import com.somemore.volunteer.domain.VolunteerDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VolunteerDetailRepository extends JpaRepository<VolunteerDetail, Long> {

    Optional<VolunteerDetail> findByVolunteerId(UUID volunteerId);
}
