package com.somemore.domains.review.repository;

import com.somemore.domains.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByIdAndDeletedFalse(Long id);

    @Query("SELECT COUNT(r) > 0 FROM Review r WHERE r.volunteerApplyId = :volunteerId AND r.deleted = false")
    boolean existsByVolunteerApplyId(Long volunteerId);

}
