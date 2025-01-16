package com.somemore.volunteer.repository;

import com.somemore.volunteer.domain.NEWVolunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("newVolunteerJpaRepository")
public interface NEWVolunteerJpaRepository extends JpaRepository<NEWVolunteer, UUID> {
}
