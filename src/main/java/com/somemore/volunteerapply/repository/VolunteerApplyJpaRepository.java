package com.somemore.volunteerapply.repository;

import com.somemore.volunteerapply.domain.VolunteerApply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerApplyJpaRepository extends JpaRepository<VolunteerApply, Long> {
}
