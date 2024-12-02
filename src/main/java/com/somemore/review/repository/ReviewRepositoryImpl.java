package com.somemore.review.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.recruitboard.domain.QRecruitBoard;
import com.somemore.recruitboard.domain.VolunteerType;
import com.somemore.review.domain.QReview;
import com.somemore.review.domain.Review;
import com.somemore.review.dto.condition.ReviewSearchCondition;
import com.somemore.volunteerapply.domain.QVolunteerApply;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ReviewRepositoryImpl implements ReviewRepository {

    private final ReviewJpaRepository reviewJpaRepository;
    private final JPAQueryFactory queryFactory;

    private final static QReview review = QReview.review;
    private final static QVolunteerApply volunteerApply = QVolunteerApply.volunteerApply;
    private final static QRecruitBoard recruitBoard = QRecruitBoard.recruitBoard;

    @Override
    public Review save(Review review) {
        return reviewJpaRepository.save(review);
    }

    @Override
    public List<Review> saveAll(List<Review> reviews) {
        return reviewJpaRepository.saveAll(reviews);
    }

    @Override
    public Optional<Review> findById(Long id) {
        return reviewJpaRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    public boolean existsByVolunteerApplyId(Long volunteerApplyId) {
        return reviewJpaRepository.existsByVolunteerApplyId(volunteerApplyId);
    }

    @Override
    public Page<Review> findAllByVolunteerIdAndSearch(UUID volunteerId,
            ReviewSearchCondition condition) {

        BooleanExpression predicate = review.volunteerId.eq(volunteerId)
                .and(eqVolunteerType(condition.volunteerType()))
                .and(isNotDeleted());

        return getReviews(condition, predicate);

    }

    @Override
    public Page<Review> findAllByCenterIdAndSearch(UUID centerId, ReviewSearchCondition condition) {

        BooleanExpression predicate = recruitBoard.centerId.eq(centerId)
                .and(eqVolunteerType(condition.volunteerType()))
                .and(isNotDeleted());

        return getReviews(condition, predicate);
    }

    @NotNull
    private Page<Review> getReviews(ReviewSearchCondition condition, BooleanExpression predicate) {
        List<Review> content = queryFactory.select(review)
                .from(review)
                .join(volunteerApply).on(review.volunteerApplyId.eq(volunteerApply.id))
                .join(recruitBoard).on(recruitBoard.id.eq(volunteerApply.recruitBoardId))
                .where(predicate)
                .offset(condition.pageable().getOffset())
                .limit(condition.pageable().getPageSize())
                .orderBy(toOrderSpecifiers(condition.pageable().getSort()))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(review.count())
                .join(volunteerApply).on(review.volunteerApplyId.eq(volunteerApply.id))
                .join(recruitBoard).on(recruitBoard.id.eq(volunteerApply.recruitBoardId))
                .from(review)
                .where(predicate);

        return PageableExecutionUtils.getPage(content, condition.pageable(), countQuery::fetchOne);
    }

    private BooleanExpression isNotDeleted() {
        return review.deleted.isFalse();
    }

    private BooleanExpression eqVolunteerType(VolunteerType type) {
        return type != null
                ? recruitBoard.recruitmentInfo.volunteerType.eq(type) : null;
    }

    private OrderSpecifier<?>[] toOrderSpecifiers(Sort sort) {
        return sort.stream()
                .map(order -> {
                    String property = order.getProperty();

                    if ("created_at".equals(property)) {
                        return order.isAscending()
                                ? review.createdAt.asc()
                                : review.createdAt.desc();
                    }
                    if ("updated_at".equals(property)) {
                        return order.isAscending()
                                ? review.updatedAt.asc()
                                : review.updatedAt.desc();
                    }

                    throw new IllegalStateException("Invalid sort property: " + property);

                })
                .toArray(OrderSpecifier[]::new);
    }
}
