package com.somemore.review.repository;

import com.somemore.review.domain.Review;
import java.util.Optional;

public interface ReviewRepository {

    Review save(Review review);

    Optional<Review> findById(Long id);

    boolean existsByVolunteerApplyId(Long volunteerApplyId);
}
