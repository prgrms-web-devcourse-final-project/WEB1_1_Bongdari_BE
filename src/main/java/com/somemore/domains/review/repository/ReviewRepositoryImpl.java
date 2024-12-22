package com.somemore.domains.review.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.domains.recruitboard.domain.QRecruitBoard;
import com.somemore.domains.recruitboard.domain.VolunteerCategory;
import com.somemore.domains.review.domain.QReview;
import com.somemore.domains.review.domain.Review;
import com.somemore.domains.review.dto.condition.ReviewSearchCondition;
import com.somemore.domains.volunteerapply.domain.QVolunteerApply;
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

    private static final QReview review = QReview.review;
    private static final QVolunteerApply volunteerApply = QVolunteerApply.volunteerApply;
    private static final QRecruitBoard recruitBoard = QRecruitBoard.recruitBoard;

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

        BooleanExpression exp = volunteerIdEq(volunteerId)
                .and(volunteerCategoryEq(condition.category()))
                .and(isNotDeleted());

        return getReviews(condition, exp);

    }

    @Override
    public Page<Review> findAllByCenterIdAndSearch(UUID centerId, ReviewSearchCondition condition) {

        BooleanExpression exp = centerIdEq(centerId)
                .and(volunteerCategoryEq(condition.category()))
                .and(isNotDeleted());

        return getReviews(condition, exp);
    }

    @NotNull
    private Page<Review> getReviews(ReviewSearchCondition condition, BooleanExpression exp) {
        List<Review> content = queryFactory.select(review)
                .from(review)
                .join(volunteerApply).on(review.volunteerApplyId.eq(volunteerApply.id))
                .join(recruitBoard).on(recruitBoard.id.eq(volunteerApply.recruitBoardId))
                .where(exp)
                .offset(condition.pageable().getOffset())
                .limit(condition.pageable().getPageSize())
                .orderBy(toOrderSpecifiers(condition.pageable().getSort()))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(review.count())
                .from(review)
                .join(volunteerApply).on(review.volunteerApplyId.eq(volunteerApply.id))
                .join(recruitBoard).on(recruitBoard.id.eq(volunteerApply.recruitBoardId))
                .where(exp);

        return PageableExecutionUtils.getPage(content, condition.pageable(), countQuery::fetchOne);
    }

    private static BooleanExpression volunteerIdEq(UUID volunteerId) {
        return review.volunteerId.eq(volunteerId);
    }

    private static BooleanExpression centerIdEq(UUID centerId) {
        return recruitBoard.centerId.eq(centerId);
    }

    private BooleanExpression isNotDeleted() {
        return review.deleted.isFalse();
    }

    private BooleanExpression volunteerCategoryEq(VolunteerCategory category) {
        return category != null
                ? recruitBoard.recruitmentInfo.volunteerCategory.eq(category) : null;
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
