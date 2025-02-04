package com.somemore.domains.review.service;

import static com.somemore.domains.recruitboard.domain.VolunteerCategory.COUNSELING;
import static com.somemore.domains.recruitboard.domain.VolunteerCategory.CULTURAL_EVENT;
import static com.somemore.domains.recruitboard.domain.VolunteerCategory.OTHER;
import static com.somemore.domains.volunteerapply.domain.ApplyStatus.APPROVED;
import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_REVIEW;
import static com.somemore.support.fixture.CenterFixture.createCenter;
import static com.somemore.support.fixture.RecruitBoardFixture.createCompletedRecruitBoard;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import com.somemore.domains.center.domain.Center;
import com.somemore.domains.center.repository.center.CenterRepository;
import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.domain.VolunteerCategory;
import com.somemore.domains.recruitboard.repository.RecruitBoardRepository;
import com.somemore.domains.review.domain.Review;
import com.somemore.domains.review.dto.condition.ReviewSearchCondition;
import com.somemore.domains.review.dto.response.ReviewDetailResponseDto;
import com.somemore.domains.review.dto.response.ReviewDetailWithNicknameResponseDto;
import com.somemore.domains.review.repository.ReviewRepository;
import com.somemore.domains.volunteerapply.domain.VolunteerApply;
import com.somemore.domains.volunteerapply.repository.VolunteerApplyRepository;
import com.somemore.global.exception.NoSuchElementException;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.repository.NEWVolunteerRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

class ReviewQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private ReviewQueryService reviewQueryService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RecruitBoardRepository recruitBoardRepository;

    @Autowired
    private VolunteerApplyRepository volunteerApplyRepository;

    @Autowired
    private NEWVolunteerRepository volunteerRepository;

    @Autowired
    private CenterRepository centerRepository;

    private Long volunteerApplyId;
    private Review review;

    @BeforeEach
    void setUp() {
        volunteerApplyId = 100L;
        review = createReview(volunteerApplyId, UUID.randomUUID());
        reviewRepository.save(review);
    }

    @DisplayName("봉사 지원 아이디로 리뷰 존재 유무를 조회할 수 있다.")
    @Test
    void existsByVolunteerApplyId() {
        // given
        // when
        boolean result = reviewQueryService.existsByVolunteerApplyId(volunteerApplyId);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("아이디로 리뷰를 조회할 수 있다.")
    @Test
    void getById() {
        // given
        Long id = review.getId();

        // when
        Review findOne = reviewQueryService.getById(id);

        // then
        assertThat(findOne.getId()).isEqualTo(id);
    }

    @DisplayName("존재하지 않는 아이디로 리뷰를 조회하면 에러가 발생한다.")
    @Test
    void getByIdWhenDoesNotExist() {
        // given
        Long wrongId = 10000000L;

        // when
        // then
        assertThatThrownBy(
                () -> reviewQueryService.getById(wrongId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(NOT_EXISTS_REVIEW.getMessage());
    }

    @DisplayName("아이디로 리뷰를 상세 조회할 수 있다.")
    @Test
    void getDetailById() {
        // given
        Long boardId = 1234L;
        VolunteerApply apply = createApply(UUID.randomUUID(), boardId);
        volunteerApplyRepository.save(apply);

        Review review = createReview(apply.getId(), UUID.randomUUID(), "제 인생 최고의 봉사활동",
                "정말 유익했습니다. 더보기..");
        reviewRepository.save(review);

        Long id = review.getId();

        // when
        ReviewDetailResponseDto findOne = reviewQueryService.getDetailById(id);

        // then
        assertThat(findOne).extracting("id").isEqualTo(review.getId());
        assertThat(findOne).extracting("volunteerId").isEqualTo(review.getVolunteerId());
        assertThat(findOne).extracting("volunteerApplyId").isEqualTo(review.getVolunteerApplyId());
        assertThat(findOne).extracting("recruitBoardId").isEqualTo(boardId);
        assertThat(findOne).extracting("title").isEqualTo(review.getTitle());
        assertThat(findOne).extracting("content").isEqualTo(review.getContent());
    }


    @DisplayName("봉사자 ID로 리뷰 리스트를 조회할 수 있다.")
    @Test
    void getReviewsByVolunteerId() {
        // given
        NEWVolunteer volunteer = createVolunteer();
        volunteerRepository.save(volunteer);

        String nickname = volunteer.getNickname();
        UUID volunteerId = volunteer.getId();

        VolunteerCategory category = CULTURAL_EVENT;
        RecruitBoard board1 = createCompletedRecruitBoard(category);
        RecruitBoard board2 = createCompletedRecruitBoard(OTHER);
        recruitBoardRepository.saveAll(List.of(board1, board2));

        VolunteerApply apply1 = createApply(volunteerId, board1.getId());
        VolunteerApply apply2 = createApply(volunteerId, board2.getId());

        volunteerApplyRepository.saveAll(List.of(apply1, apply2));

        Review review1 = createReview(apply1.getId(), volunteerId, "제 인생 최고의 봉사활동",
                "정말 유익했습니다. 더보기..");
        Review review2 = createReview(apply2.getId(), volunteerId, "보람있는 봉사활동",
                "많은 사람들에게 도움을 주었어요.");
        reviewRepository.saveAll(List.of(review1, review2));

        ReviewSearchCondition conditionWithoutCategory = ReviewSearchCondition.builder()
                .pageable(getPageable())
                .build();

        // when
        Page<ReviewDetailWithNicknameResponseDto> result = reviewQueryService.getDetailsWithNicknameByVolunteerId(
                volunteerId,
                conditionWithoutCategory);

        // then
        assertThat(result.getContent())
                .extracting("id", "title", "volunteerNickname")
                .containsExactlyInAnyOrder(
                        tuple(review1.getId(), "제 인생 최고의 봉사활동", nickname),
                        tuple(review2.getId(), "보람있는 봉사활동", nickname)
                );

        assertThat(result.getPageable().getPageSize()).isEqualTo(5);
        assertThat(result.getPageable().getPageNumber()).isZero();
    }

    @DisplayName("센터 ID로 리뷰 리스트를 조회할 수 있다.")
    @Test
    void getReviewsByCenterId() {
        // given
        Center center = createCenter("Test Center");
        centerRepository.save(center);

        RecruitBoard board1 = createCompletedRecruitBoard(center.getId(), COUNSELING);
        RecruitBoard board2 = createCompletedRecruitBoard(center.getId(), CULTURAL_EVENT);
        recruitBoardRepository.saveAll(List.of(board1, board2));

        NEWVolunteer volunteer1 = createVolunteer();
        NEWVolunteer volunteer2 = createVolunteer();
        volunteerRepository.save(volunteer1);
        volunteerRepository.save(volunteer2);

        VolunteerApply apply1 = createApply(volunteer1.getId(), board1.getId());
        VolunteerApply apply2 = createApply(volunteer2.getId(), board2.getId());
        volunteerApplyRepository.saveAll(List.of(apply1, apply2));

        Review review1 = createReview(apply1.getId(), volunteer1.getId(), "제 인생 최고의 봉사활동",
                "정말 유익했습니다. 더보기..");
        Review review2 = createReview(apply2.getId(), volunteer2.getId(), "보람있는 봉사활동",
                "많은 사람들에게 도움을 주었어요.");
        reviewRepository.saveAll(List.of(review1, review2));

        ReviewSearchCondition condition = ReviewSearchCondition.builder()
                .pageable(getPageable())
                .build();

        // when
        Page<ReviewDetailWithNicknameResponseDto> result = reviewQueryService.getDetailsWithNicknameByCenterId(
                center.getId(),
                condition);

        // then
        assertThat(result.getContent())
                .extracting("id", "title", "volunteerNickname")
                .containsExactlyInAnyOrder(
                        tuple(review1.getId(), "제 인생 최고의 봉사활동", volunteer1.getNickname()),
                        tuple(review2.getId(), "보람있는 봉사활동", volunteer2.getNickname())
                );

        assertThat(result.getPageable().getPageSize()).isEqualTo(5);
        assertThat(result.getPageable().getPageNumber()).isZero();
    }

    private static NEWVolunteer createVolunteer() {
        return NEWVolunteer.createDefault(UUID.randomUUID());
    }

    private Review createReview(Long applyId, UUID volunteerId) {
        return Review.builder()
                .volunteerApplyId(applyId)
                .volunteerId(volunteerId)
                .title("리뷰 제목")
                .content("리뷰 내용")
                .build();
    }

    private Review createReview(Long applyId, UUID volunteerId, String title, String content) {
        return Review.builder()
                .volunteerApplyId(applyId)
                .volunteerId(volunteerId)
                .title(title)
                .content(content)
                .build();
    }

    private VolunteerApply createApply(UUID volunteerId, Long recruitId) {
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
