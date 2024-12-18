package com.somemore.domains.volunteer.repository;

import com.somemore.domains.volunteer.domain.VolunteerDetail;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VolunteerDetailRepository {
    VolunteerDetail save(VolunteerDetail volunteerDetail);
    Optional<VolunteerDetail> findByVolunteerId(UUID volunteerId);
}
