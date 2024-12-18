package com.somemore.domains.volunteer.repository;

import com.somemore.domains.volunteer.domain.VolunteerDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerDetailJpaRepository extends JpaRepository<VolunteerDetail, Long> {
}
