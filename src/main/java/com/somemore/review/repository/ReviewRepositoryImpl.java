package com.somemore.review.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.review.domain.Review;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ReviewRepositoryImpl implements ReviewRepository {

    private final ReviewJpaRepository reviewJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Review save(Review review) {
        return reviewJpaRepository.save(review);
    }

    @Override
    public Optional<Review> findById(Long id) {
        return reviewJpaRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    public boolean existsByVolunteerApplyId(Long volunteerApplyId) {
        return reviewJpaRepository.existsByVolunteerApplyId(volunteerApplyId);
    }
}
