package com.somemore.center.repository;

import com.somemore.center.domain.Center_NEW;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CenterJpaRepository extends JpaRepository<Center_NEW, UUID> {
}
