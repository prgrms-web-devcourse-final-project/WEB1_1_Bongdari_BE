package com.somemore.domains.volunteerapply.repository;

import com.somemore.domains.volunteerapply.domain.VolunteerApply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerApplyJpaRepository extends JpaRepository<VolunteerApply, Long> {
}
