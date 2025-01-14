package com.somemore.domains.volunteerrecord.repository;

import com.somemore.domains.volunteerrecord.domain.VolunteerRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerRecordJpaRepository extends JpaRepository<VolunteerRecord, Long> {
}
