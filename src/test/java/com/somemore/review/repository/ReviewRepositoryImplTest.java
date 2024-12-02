package com.somemore.review.repository;

import static com.somemore.auth.oauth.OAuthProvider.NAVER;
import static com.somemore.common.fixture.CenterFixture.createCenter;
import static com.somemore.common.fixture.RecruitBoardFixture.createCompletedRecruitBoard;
import static com.somemore.recruitboard.domain.VolunteerType.COUNSELING;
import static com.somemore.recruitboard.domain.VolunteerType.CULTURAL_EVENT;
import static com.somemore.recruitboard.domain.VolunteerType.OTHER;
import static com.somemore.volunteerapply.domain.ApplyStatus.APPROVED;
import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.IntegrationTestSupport;
import com.somemore.auth.oauth.OAuthProvider;
import com.somemore.center.domain.Center;
import com.somemore.center.repository.CenterRepository;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.domain.VolunteerType;
import com.somemore.recruitboard.repository.RecruitBoardRepository;
import com.somemore.review.domain.Review;
import com.somemore.review.dto.condition.ReviewSearchCondition;
import com.somemore.volunteer.domain.Volunteer;
import com.somemore.volunteer.repository.VolunteerRepository;
import com.somemore.volunteerapply.domain.ApplyStatus;
import com.somemore.volunteerapply.domain.VolunteerApply;
import com.somemore.volunteerapply.repository.VolunteerApplyRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

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

    @DisplayName("리뷰 생성 및 조회")
    @Test
    void saveAndFind() {
        // given
        Review review = Review.builder()
                .volunteerApplyId(1L)
                .volunteerId(UUID.randomUUID())
                .title("리뷰 제목")
                .content("리뷰 내용")
                .imgUrl("")
                .build();
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
        Review review = Review.builder()
                .volunteerApplyId(volunteerApplyId)
                .volunteerId(UUID.randomUUID())
                .title("리뷰 제목")
                .content("리뷰 내용")
                .imgUrl("")
                .build();
        reviewRepository.save(review);

        // when
        boolean result = reviewRepository.existsByVolunteerApplyId(volunteerApplyId);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("봉사자 ID로 검색 조건으로 리뷰를 조회할 수 있다.")
    @Test
    void findAllByVolunteerIdAndSearchWithCondition() {
        // given
        UUID volunteerId = UUID.randomUUID();

        VolunteerType type = CULTURAL_EVENT;
        RecruitBoard board1 = createCompletedRecruitBoard(type);
        RecruitBoard board2 = createCompletedRecruitBoard(OTHER);
        recruitBoardRepository.saveAll(List.of(board1, board2));

        VolunteerApply apply1 = createApply(volunteerId, board1.getId());
        VolunteerApply apply2 = createApply(volunteerId, board2.getId());

        volunteerApplyRepository.saveAll(List.of(apply1, apply2));

        Review review1 = createReview(apply1.getId(), volunteerId, "제 인생 최고의 봉사활동",
                "정말 유익했습니다. 더보기..",
                "https://image.domain.com/links1");
        Review review2 = createReview(apply2.getId(), volunteerId, "보람있는 봉사활동",
                "많은 사람들에게 도움을 주었어요.",
                "https://image.domain.com/links2");
        reviewRepository.saveAll(List.of(review1, review2));

        ReviewSearchCondition conditionWithType = ReviewSearchCondition.builder()
                .volunteerType(type)
                .pageable(getPageable())
                .build();

        // when
        Page<Review> result = reviewRepository.findAllByVolunteerIdAndSearch(volunteerId,
                conditionWithType);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).extracting("title")
                .containsExactlyInAnyOrder("제 인생 최고의 봉사활동");

        assertThat(result.getPageable().getPageSize()).isEqualTo(5);
        assertThat(result.getPageable().getPageNumber()).isZero();
    }

    @DisplayName("봉사자 ID로 리뷰를 조회할 수 있다")
    @Test
    void findAllByVolunteerIdAndSearchWithoutCondition() {
        // given
        UUID volunteerId = UUID.randomUUID();

        VolunteerType type = CULTURAL_EVENT;
        RecruitBoard board1 = createCompletedRecruitBoard(type);
        RecruitBoard board2 = createCompletedRecruitBoard(OTHER);
        recruitBoardRepository.saveAll(List.of(board1, board2));

        VolunteerApply apply1 = createApply(volunteerId, board1.getId());
        VolunteerApply apply2 = createApply(volunteerId, board2.getId());

        volunteerApplyRepository.saveAll(List.of(apply1, apply2));

        Review review1 = createReview(apply1.getId(), volunteerId, "제 인생 최고의 봉사활동",
                "정말 유익했습니다. 더보기..",
                "https://image.domain.com/links1");
        Review review2 = createReview(apply2.getId(), volunteerId, "보람있는 봉사활동",
                "많은 사람들에게 도움을 주었어요.",
                "https://image.domain.com/links2");
        reviewRepository.saveAll(List.of(review1, review2));

        ReviewSearchCondition conditionWithoutType = ReviewSearchCondition.builder()
                .pageable(getPageable())
                .build();

        // when
        Page<Review> result = reviewRepository.findAllByVolunteerIdAndSearch(volunteerId,
                conditionWithoutType);

        // then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).extracting("title")
                .containsExactlyInAnyOrder("제 인생 최고의 봉사활동", "보람있는 봉사활동");

        assertThat(result.getPageable().getPageSize()).isEqualTo(5);
        assertThat(result.getPageable().getPageNumber()).isZero();
    }

    @DisplayName("기관 ID와 검색 조건으로 리뷰 목록을 조회할 수 있다.")
    @Test
    void findAllByCenterIdAndSearchWithType() {
        // given
        Center center = createCenter("Test Center");
        centerRepository.save(center);

        RecruitBoard board1 = createCompletedRecruitBoard(center.getId(), COUNSELING);
        RecruitBoard board2 = createCompletedRecruitBoard(center.getId(), CULTURAL_EVENT);
        recruitBoardRepository.saveAll(List.of(board1, board2));

        Volunteer volunteer = Volunteer.createDefault(NAVER, "naver");
        volunteerRepository.save(volunteer);

        VolunteerApply apply1 = createApply(volunteer.getId(), board1.getId());
        VolunteerApply apply2 = createApply(volunteer.getId(), board2.getId());
        volunteerApplyRepository.saveAll(List.of(apply1, apply2));

        Review review1 = createReview(apply1.getId(), volunteer.getId(), "제목제목", "내용내용", "이미지링크");
        Review review2 = createReview(apply2.getId(), volunteer.getId(), "제목제목제목", "내용내용내용",
                "이미지링크링크");
        reviewRepository.saveAll(List.of(review1, review2));

        ReviewSearchCondition condition = ReviewSearchCondition.builder()
                .volunteerType(CULTURAL_EVENT)
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

    @DisplayName("기관 ID으로 리뷰 목록을 조회할 수 있다.")
    @Test
    void findAllByCenterIdAndSearchWithoutType() {
        // given
        Center center = createCenter("Test Center");
        centerRepository.save(center);

        RecruitBoard board1 = createCompletedRecruitBoard(center.getId(), COUNSELING);
        RecruitBoard board2 = createCompletedRecruitBoard(center.getId(), CULTURAL_EVENT);
        recruitBoardRepository.saveAll(List.of(board1, board2));

        Volunteer volunteer = Volunteer.createDefault(NAVER, "naver");
        volunteerRepository.save(volunteer);

        VolunteerApply apply1 = createApply(volunteer.getId(), board1.getId());
        VolunteerApply apply2 = createApply(volunteer.getId(), board2.getId());
        volunteerApplyRepository.saveAll(List.of(apply1, apply2));

        Review review1 = createReview(apply1.getId(), volunteer.getId(), "제목제목", "내용내용", "이미지링크");
        Review review2 = createReview(apply2.getId(), volunteer.getId(), "제목제목제목", "내용내용내용",
                "이미지링크링크");
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

    private Review createReview(Long applyId, UUID volunteerId, String title, String content,
            String imgUrl) {
        return Review.builder()
                .volunteerApplyId(applyId)
                .volunteerId(volunteerId)
                .title(title)
                .content(content)
                .imgUrl(imgUrl)
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
