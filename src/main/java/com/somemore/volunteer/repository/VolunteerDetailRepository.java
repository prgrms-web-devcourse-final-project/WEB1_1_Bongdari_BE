package com.somemore.volunteer.repository;

import com.somemore.volunteer.domain.VolunteerDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerDetailRepository extends JpaRepository<VolunteerDetail, Long> {
}
