package com.somemore.review.repository;

import com.somemore.review.domain.Review;
import com.somemore.review.dto.condition.ReviewSearchCondition;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface ReviewRepository {

    Review save(Review review);

    List<Review> saveAll(List<Review> reviews);

    Optional<Review> findById(Long id);

    boolean existsByVolunteerApplyId(Long volunteerApplyId);

    Page<Review> findAllByVolunteerIdAndSearch(UUID volunteerId, ReviewSearchCondition condition);

    Page<Review> findAllByCenterIdAndSearch(UUID centerId, ReviewSearchCondition condition);
}
