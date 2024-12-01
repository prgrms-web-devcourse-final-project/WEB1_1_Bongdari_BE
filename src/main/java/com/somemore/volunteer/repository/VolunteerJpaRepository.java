package com.somemore.volunteer.repository;

import com.somemore.volunteer.domain.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerJpaRepository extends JpaRepository<Volunteer, Long> {
}
