package com.somemore.domains.location.repository;

import com.somemore.domains.location.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationJpaRepository extends JpaRepository<Location, Long> {

}
