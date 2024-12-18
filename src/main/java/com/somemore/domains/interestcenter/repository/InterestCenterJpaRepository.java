package com.somemore.domains.interestcenter.repository;

import com.somemore.domains.interestcenter.domain.InterestCenter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestCenterJpaRepository extends JpaRepository<InterestCenter, Long> {
}
