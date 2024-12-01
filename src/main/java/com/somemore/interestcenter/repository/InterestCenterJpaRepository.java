package com.somemore.interestcenter.repository;

import com.somemore.interestcenter.domain.InterestCenter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestCenterJpaRepository extends JpaRepository<InterestCenter, Long> {
}
