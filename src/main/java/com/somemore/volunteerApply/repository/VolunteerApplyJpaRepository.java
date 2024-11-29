package com.somemore.volunteerApply.repository;

import com.somemore.volunteerApply.domain.VolunteerApply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerApplyJpaRepository extends JpaRepository<VolunteerApply, Long> {
}
