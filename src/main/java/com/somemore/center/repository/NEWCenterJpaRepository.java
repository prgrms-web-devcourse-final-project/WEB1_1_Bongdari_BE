package com.somemore.center.repository;

import com.somemore.center.domain.NEWCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("newCenterJpaRepository")
public interface NEWCenterJpaRepository extends JpaRepository<NEWCenter, UUID> {
}
