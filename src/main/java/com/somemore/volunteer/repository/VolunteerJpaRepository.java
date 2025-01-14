package com.somemore.volunteer.repository;

import com.somemore.volunteer.domain.Volunteer_NEW;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VolunteerJpaRepository extends JpaRepository<Volunteer_NEW, UUID> {
}
