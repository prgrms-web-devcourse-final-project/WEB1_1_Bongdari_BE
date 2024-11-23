package com.somemore.location.repository;

import com.somemore.location.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationJpaRepository extends JpaRepository<Location, Long> {

}
