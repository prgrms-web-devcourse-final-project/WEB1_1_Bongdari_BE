package com.somemore.domains.review.repository;

import com.somemore.domains.center.domain.Center;
import com.somemore.domains.center.repository.center.CenterRepository;
import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.domain.VolunteerCategory;
import com.somemore.domains.recruitboard.repository.RecruitBoardRepository;
import com.somemore.domains.review.domain.Review;
import com.somemore.domains.review.dto.condition.ReviewSearchCondition;
import com.somemore.domains.volunteer.domain.Volunteer;
import com.somemore.domains.volunteer.repository.VolunteerRepository;
import com.somemore.domains.volunteerapply.domain.VolunteerApply;
import com.somemore.domains.volunteerapply.repository.VolunteerApplyRepository;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.somemore.domains.recruitboard.domain.VolunteerCategory.*;
import static com.somemore.domains.volunteerapply.domain.ApplyStatus.APPROVED;
import static com.somemore.global.auth.oauth.domain.OAuthProvider.NAVER;
import static com.somemore.support.fixture.CenterFixture.createCenter;
import static com.somemore.support.fixture.RecruitBoardFixture.createCompletedRecruitBoard;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class ReviewRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private ReviewRepositoryImpl reviewRepository;

    @Autowired
    private RecruitBoardRepository recruitBoardRepository;

    @Autowired
    private VolunteerApplyRepository volunteerApplyRepository;

    @Autowired
    private CenterRepository centerRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @DisplayName("리뷰를 저장하고 조회할 수 있다")
    @Test
    void saveAndFind() {
        // given
        Review review = createReview(1L, UUID.randomUUID(), "리뷰 내용");
        reviewRepository.save(review);

        // when
        Optional<Review> findReview = reviewRepository.findById(review.getId());

        // then
        assertThat(findReview).isPresent();
        assertThat(findReview.get().getId()).isEqualTo(review.getId());
    }

    @DisplayName("봉사 지원 아이디로 리뷰 존재 유무를 확인할 수 있다")
    @Test
    void existsByVolunteerApplyId() {
        // given
        Long volunteerApplyId = 1L;
        Review review = createReview(volunteerApplyId, UUID.randomUUID(), "리뷰 내용");
        reviewRepository.save(review);

        // when
        boolean result = reviewRepository.existsByVolunteerApplyId(volunteerApplyId);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("봉사자 ID로 조건 없이 리뷰를 조회할 수 있다")
    @Test
    void findAllByVolunteerIdAndSearchWithoutCondition() {
        // given
        UUID volunteerId = UUID.randomUUID();

        VolunteerCategory category = CULTURAL_EVENT;
        RecruitBoard board1 = createCompletedRecruitBoard(category);
        RecruitBoard board2 = createCompletedRecruitBoard(OTHER);
        recruitBoardRepository.saveAll(List.of(board1, board2));

        VolunteerApply apply1 = createApply(volunteerId, board1.getId());
        VolunteerApply apply2 = createApply(volunteerId, board2.getId());

        volunteerApplyRepository.saveAll(List.of(apply1, apply2));

        Review review1 = createReview(apply1.getId(), volunteerId, "제 인생 최고의 봉사활동");
        Review review2 = createReview(apply2.getId(), volunteerId, "보람있는 봉사활동");
        reviewRepository.saveAll(List.of(review1, review2));

        ReviewSearchCondition conditionWithoutCategory = ReviewSearchCondition.builder()
                .pageable(getPageable())
                .build();

        // when
        Page<Review> result = reviewRepository.findAllByVolunteerIdAndSearch(volunteerId,
                conditionWithoutCategory);

        // then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).extracting("title")
                .containsExactlyInAnyOrder("제 인생 최고의 봉사활동", "보람있는 봉사활동");

        assertThat(result.getPageable().getPageSize()).isEqualTo(5);
        assertThat(result.getPageable().getPageNumber()).isZero();
    }

    @DisplayName("기관 ID와 봉사 유형으로 리뷰 목록을 조회할 수 있다")
    @Test
    void findAllByVolunteerIdAndSearchWithCondition() {
        // given
        UUID volunteerId = UUID.randomUUID();

        VolunteerCategory category = CULTURAL_EVENT;
        RecruitBoard board1 = createCompletedRecruitBoard(category);
        RecruitBoard board2 = createCompletedRecruitBoard(OTHER);
        recruitBoardRepository.saveAll(List.of(board1, board2));

        VolunteerApply apply1 = createApply(volunteerId, board1.getId());
        VolunteerApply apply2 = createApply(volunteerId, board2.getId());

        volunteerApplyRepository.saveAll(List.of(apply1, apply2));

        Review review1 = createReview(apply1.getId(), volunteerId, "제 인생 최고의 봉사활동");
        Review review2 = createReview(apply2.getId(), volunteerId, "보람있는 봉사활동");
        reviewRepository.saveAll(List.of(review1, review2));

        ReviewSearchCondition conditionWithCategory = ReviewSearchCondition.builder()
                .category(category)
                .pageable(getPageable())
                .build();

        // when
        Page<Review> result = reviewRepository.findAllByVolunteerIdAndSearch(volunteerId,
                conditionWithCategory);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).extracting("title")
                .containsExactlyInAnyOrder("제 인생 최고의 봉사활동");

        assertThat(result.getPageable().getPageSize()).isEqualTo(5);
        assertThat(result.getPageable().getPageNumber()).isZero();
    }

    @DisplayName("기관 ID로 봉사 유형 없이 리뷰 목록을 조회할 수 있다")
    @Test
    void findAllByCenterIdAndSearchWithoutCategory() {
        // given
        Center center = createCenter("Test Center");
        centerRepository.save(center);

        RecruitBoard board1 = createCompletedRecruitBoard(center.getId(), COUNSELING);
        RecruitBoard board2 = createCompletedRecruitBoard(center.getId(), CULTURAL_EVENT);
        recruitBoardRepository.saveAll(List.of(board1, board2));

        Volunteer volunteer = createVolunteer();
        volunteerRepository.save(volunteer);

        VolunteerApply apply1 = createApply(volunteer.getId(), board1.getId());
        VolunteerApply apply2 = createApply(volunteer.getId(), board2.getId());
        volunteerApplyRepository.saveAll(List.of(apply1, apply2));

        Review review1 = createReview(apply1.getId(), volunteer.getId(), "제목제목");
        Review review2 = createReview(apply2.getId(), volunteer.getId(), "제목제목제목");
        reviewRepository.saveAll(List.of(review1, review2));

        ReviewSearchCondition condition = ReviewSearchCondition.builder()
                .pageable(getPageable())
                .build();

        // when
        Page<Review> result = reviewRepository.findAllByCenterIdAndSearch(center.getId(),
                condition);

        assertThat(result.getContent()).hasSize(2);

        assertThat(result.getContent())
                .extracting("title")
                .containsExactlyInAnyOrder("제목제목", "제목제목제목");

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @DisplayName("기관 ID와 검색 조건으로 리뷰 목록을 조회할 수 있다.")
    @Test
    void findAllByCenterIdAndSearchWithCategory() {
        // given
        Center center = createCenter("Test Center");
        centerRepository.save(center);

        RecruitBoard board1 = createCompletedRecruitBoard(center.getId(), COUNSELING);
        RecruitBoard board2 = createCompletedRecruitBoard(center.getId(), CULTURAL_EVENT);
        recruitBoardRepository.saveAll(List.of(board1, board2));

        Volunteer volunteer = createVolunteer();
        volunteerRepository.save(volunteer);

        VolunteerApply apply1 = createApply(volunteer.getId(), board1.getId());
        VolunteerApply apply2 = createApply(volunteer.getId(), board2.getId());
        volunteerApplyRepository.saveAll(List.of(apply1, apply2));

        Review review1 = createReview(apply1.getId(), volunteer.getId(), "제목제목");
        Review review2 = createReview(apply2.getId(), volunteer.getId(), "제목제목제목");
        reviewRepository.saveAll(List.of(review1, review2));

        ReviewSearchCondition condition = ReviewSearchCondition.builder()
                .category(CULTURAL_EVENT)
                .pageable(getPageable())
                .build();

        // when
        Page<Review> result = reviewRepository.findAllByCenterIdAndSearch(center.getId(),
                condition);

        assertThat(result.getContent()).hasSize(1);

        assertThat(result.getContent())
                .extracting("title")
                .containsExactly("제목제목제목");

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

    private static Volunteer createVolunteer() {
        return Volunteer.createDefault(NAVER, "naver");
    }

    private Review createReview(Long applyId, UUID volunteerId, String title) {
        return Review.builder()
                .volunteerApplyId(applyId)
                .volunteerId(volunteerId)
                .title(title)
                .content("내용내용")
                .build();
    }

    private static VolunteerApply createApply(UUID volunteerId, Long recruitId) {
        return VolunteerApply.builder()
                .volunteerId(volunteerId)
                .recruitBoardId(recruitId)
                .status(APPROVED)
                .attended(false)
                .build();
    }

    private Pageable getPageable() {
        Sort sort = Sort.by(Sort.Order.desc("created_at"));
        return PageRequest.of(0, 5, sort);
    }

}
