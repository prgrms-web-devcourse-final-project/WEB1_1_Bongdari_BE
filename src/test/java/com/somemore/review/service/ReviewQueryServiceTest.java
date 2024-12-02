package com.somemore.review.service;

import static com.somemore.auth.oauth.OAuthProvider.NAVER;
import static com.somemore.common.fixture.CenterFixture.createCenter;
import static com.somemore.common.fixture.RecruitBoardFixture.createCompletedRecruitBoard;
import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_REVIEW;
import static com.somemore.recruitboard.domain.VolunteerType.COUNSELING;
import static com.somemore.recruitboard.domain.VolunteerType.CULTURAL_EVENT;
import static com.somemore.recruitboard.domain.VolunteerType.OTHER;
import static com.somemore.volunteerapply.domain.ApplyStatus.APPROVED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import com.somemore.IntegrationTestSupport;
import com.somemore.center.domain.Center;
import com.somemore.center.repository.CenterRepository;
import com.somemore.global.exception.BadRequestException;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.domain.VolunteerType;
import com.somemore.recruitboard.repository.RecruitBoardRepository;
import com.somemore.review.domain.Review;
import com.somemore.review.dto.condition.ReviewSearchCondition;
import com.somemore.review.dto.response.ReviewResponseDto;
import com.somemore.review.dto.response.ReviewWithNicknameResponseDto;
import com.somemore.review.repository.ReviewRepository;
import com.somemore.volunteer.domain.Volunteer;
import com.somemore.volunteer.repository.VolunteerRepository;
import com.somemore.volunteerapply.domain.VolunteerApply;
import com.somemore.volunteerapply.repository.VolunteerApplyRepository;
import java.util.List;
import java.util.UUID;
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
    private VolunteerRepository volunteerRepository;

    @Autowired
    private CenterRepository centerRepository;

    @DisplayName("아이디로 리뷰를 조회할 수 있다.")
    @Test
    void getReviewById() {
        // given
        Review review = createReview(1L, UUID.randomUUID());
        reviewRepository.save(review);

        // when
        ReviewResponseDto findOne = reviewQueryService.getReviewById(review.getId());

        // then
        assertThat(findOne).extracting("id").isEqualTo(review.getId());
        assertThat(findOne).extracting("volunteerId").isEqualTo(review.getVolunteerId());
        assertThat(findOne).extracting("title").isEqualTo(review.getTitle());
        assertThat(findOne).extracting("content").isEqualTo(review.getContent());
        assertThat(findOne).extracting("imgUrl").isEqualTo(review.getImgUrl());
    }

    @DisplayName("존재하지 않는 아이디로 리뷰를 조회하면 에러가 발생한다.")
    @Test
    void getReviewByIdWhenWrongId() {
        // given
        Long wrongId = 10000000L;

        // when
        // then
        assertThatThrownBy(
                () -> reviewQueryService.getReviewById(wrongId)
        ).isInstanceOf(BadRequestException.class)
                .hasMessage(NOT_EXISTS_REVIEW.getMessage());
    }

    @DisplayName("봉사자 ID로 리뷰 리스트를 조회할 수 있다.")
    @Test
    void getReviewsByVolunteerId() {
        // given
        Volunteer volunteer = Volunteer.createDefault(NAVER, "naver");
        volunteerRepository.save(volunteer);

        String nickname = volunteer.getNickname();
        UUID volunteerId = volunteer.getId();

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
        Page<ReviewWithNicknameResponseDto> result = reviewQueryService.getReviewsByVolunteerId(
                volunteerId,
                conditionWithoutType);

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

        Volunteer volunteer1 = Volunteer.createDefault(NAVER, "naver");
        Volunteer volunteer2 = Volunteer.createDefault(NAVER, "naver");
        volunteer2.markAsDeleted();
        volunteerRepository.save(volunteer1);
        volunteerRepository.save(volunteer2);

        VolunteerApply apply1 = createApply(volunteer1.getId(), board1.getId());
        VolunteerApply apply2 = createApply(volunteer2.getId(), board2.getId());
        volunteerApplyRepository.saveAll(List.of(apply1, apply2));

        Review review1 = createReview(apply1.getId(), volunteer1.getId(), "제 인생 최고의 봉사활동",
                "정말 유익했습니다. 더보기..",
                "https://image.domain.com/links1");
        Review review2 = createReview(apply2.getId(), volunteer2.getId(), "보람있는 봉사활동",
                "많은 사람들에게 도움을 주었어요.",
                "https://image.domain.com/links2");
        reviewRepository.saveAll(List.of(review1, review2));

        ReviewSearchCondition condition = ReviewSearchCondition.builder()
                .pageable(getPageable())
                .build();

        // when
        Page<ReviewWithNicknameResponseDto> result = reviewQueryService.getReviewsByCenterId(
                center.getId(),
                condition);

        // then
        assertThat(result.getContent())
                .extracting("id", "title", "volunteerNickname")
                .containsExactlyInAnyOrder(
                        tuple(review1.getId(), "제 인생 최고의 봉사활동", volunteer1.getNickname()),
                        tuple(review2.getId(), "보람있는 봉사활동", "삭제된 아이디")
                );

        assertThat(result.getPageable().getPageSize()).isEqualTo(5);
        assertThat(result.getPageable().getPageNumber()).isZero();
    }

    private Review createReview(Long applyId, UUID volunteerId) {
        return Review.builder()
                .volunteerApplyId(applyId)
                .volunteerId(volunteerId)
                .title("리뷰 제목")
                .content("리뷰 내용")
                .imgUrl("")
                .build();
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

    private Pageable getPageable() {
        Sort sort = Sort.by(Sort.Order.desc("created_at"));
        return PageRequest.of(0, 5, sort);
    }

    private static VolunteerApply createApply(UUID volunteerId, Long recruitId) {
        return VolunteerApply.builder()
                .volunteerId(volunteerId)
                .recruitBoardId(recruitId)
                .status(APPROVED)
                .attended(false)
                .build();
    }

}
