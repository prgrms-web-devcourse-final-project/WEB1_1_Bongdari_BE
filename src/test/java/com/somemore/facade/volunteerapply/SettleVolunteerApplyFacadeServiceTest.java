package com.somemore.facade.volunteerapply;

import static com.somemore.auth.oauth.OAuthProvider.NAVER;
import static com.somemore.common.fixture.RecruitBoardFixture.createCompletedRecruitBoard;
import static com.somemore.global.exception.ExceptionMessage.RECRUIT_BOARD_ID_MISMATCH;
import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_RECRUIT_BOARD;
import static com.somemore.global.exception.ExceptionMessage.VOLUNTEER_APPLY_LIST_MISMATCH;
import static com.somemore.recruitboard.domain.VolunteerCategory.COUNSELING;
import static com.somemore.volunteerapply.domain.ApplyStatus.APPROVED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.somemore.IntegrationTestSupport;
import com.somemore.global.exception.BadRequestException;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.repository.RecruitBoardRepository;
import com.somemore.volunteer.domain.Volunteer;
import com.somemore.volunteer.repository.VolunteerRepository;
import com.somemore.volunteerapply.domain.VolunteerApply;
import com.somemore.volunteerapply.dto.request.VolunteerApplySettleRequestDto;
import com.somemore.volunteerapply.repository.VolunteerApplyRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class SettleVolunteerApplyFacadeServiceTest extends IntegrationTestSupport {

    @Autowired
    private SettleVolunteerApplyFacadeService settleVolunteerApplyFacadeService;

    @Autowired
    private RecruitBoardRepository recruitBoardRepository;

    @Autowired
    private VolunteerApplyRepository volunteerApplyRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @DisplayName("봉사 활동 지원을 정산할 수 있다.")
    @Test
    void settleVolunteerApplies() {
        // given
        UUID centerId = UUID.randomUUID();

        Volunteer volunteer1 = volunteerRepository.save(createVolunteer());
        Volunteer volunteer2 = volunteerRepository.save(createVolunteer());
        Volunteer volunteer3 = volunteerRepository.save(createVolunteer());

        RecruitBoard board = createCompletedRecruitBoard(centerId, COUNSELING);
        recruitBoardRepository.save(board);

        VolunteerApply apply1 = createApply(volunteer1.getId(), board.getId());
        VolunteerApply apply2 = createApply(volunteer2.getId(), board.getId());
        VolunteerApply apply3 = createApply(volunteer3.getId(), board.getId());
        volunteerApplyRepository.saveAll(List.of(apply1, apply2, apply3));

        int hour = board.getVolunteerHours().getHour();
        VolunteerApplySettleRequestDto dto = VolunteerApplySettleRequestDto.builder()
                .ids(List.of(apply1.getId(), apply2.getId(), apply3.getId()))
                .build();

        // when
        settleVolunteerApplyFacadeService.settleVolunteerApplies(dto, centerId);

        // then
        VolunteerApply findApply1 = volunteerApplyRepository.findById(apply1.getId()).orElseThrow();
        VolunteerApply findApply2 = volunteerApplyRepository.findById(apply2.getId()).orElseThrow();
        VolunteerApply findApply3 = volunteerApplyRepository.findById(apply3.getId()).orElseThrow();

        assertThat(findApply1.getAttended()).isTrue();
        assertThat(findApply2.getAttended()).isTrue();
        assertThat(findApply3.getAttended()).isTrue();

        Volunteer findVolunteer1 = volunteerRepository.findById(volunteer1.getId()).orElseThrow();
        Volunteer findVolunteer2 = volunteerRepository.findById(volunteer2.getId()).orElseThrow();
        Volunteer findVolunteer3 = volunteerRepository.findById(volunteer3.getId()).orElseThrow();

        assertThat(findVolunteer1.getTotalVolunteerHours()).isEqualTo(hour);
        assertThat(findVolunteer2.getTotalVolunteerHours()).isEqualTo(hour);
        assertThat(findVolunteer3.getTotalVolunteerHours()).isEqualTo(hour);

        assertThat(findVolunteer1.getTotalVolunteerCount()).isEqualTo(1);
        assertThat(findVolunteer2.getTotalVolunteerCount()).isEqualTo(1);
        assertThat(findVolunteer3.getTotalVolunteerCount()).isEqualTo(1);
    }

    @DisplayName("정산시, 지원 리스트에 존재하지 않는 지원이 있는 경우 에러가 발생한다.")
    @Test
    void settleVolunteerAppliesWhenNotExistId() {
        // given
        UUID centerId = UUID.randomUUID();

        Volunteer volunteer = volunteerRepository.save(createVolunteer());

        RecruitBoard board = createCompletedRecruitBoard(centerId, COUNSELING);
        recruitBoardRepository.save(board);

        VolunteerApply apply = createApply(volunteer.getId(), board.getId());
        volunteerApplyRepository.save(apply);

        VolunteerApplySettleRequestDto dto = VolunteerApplySettleRequestDto.builder()
                .ids(List.of(apply.getId(), 100L))
                .build();

        // when
        // then
        assertThatThrownBy(
                () -> settleVolunteerApplyFacadeService.settleVolunteerApplies(dto, centerId)
        ).isInstanceOf(BadRequestException.class)
                .hasMessage(VOLUNTEER_APPLY_LIST_MISMATCH.getMessage());

    }

    @DisplayName("정산시, 모집글이 다른 지원이 있는 경우 에러가 발생한다.")
    @Test
    void settleVolunteerAppliesWhenMismatchApplyAndRecruitBoard() {
        // given
        UUID centerId = UUID.randomUUID();

        Volunteer volunteer1 = volunteerRepository.save(createVolunteer());
        Volunteer volunteer2 = volunteerRepository.save(createVolunteer());

        RecruitBoard board = createCompletedRecruitBoard(centerId, COUNSELING);
        recruitBoardRepository.save(board);

        VolunteerApply apply1 = createApply(volunteer1.getId(), board.getId());
        VolunteerApply apply2 = createApply(volunteer2.getId(), 100L);
        volunteerApplyRepository.saveAll(List.of(apply1, apply2));

        VolunteerApplySettleRequestDto dto = VolunteerApplySettleRequestDto.builder()
                .ids(List.of(apply1.getId(), apply2.getId()))
                .build();

        // when
        // then
        assertThatThrownBy(
                () -> settleVolunteerApplyFacadeService.settleVolunteerApplies(dto, centerId)
        ).isInstanceOf(BadRequestException.class)
                .hasMessage(RECRUIT_BOARD_ID_MISMATCH.getMessage());
    }

    @DisplayName("정산시, 기관이 작성한 모집글이 아닌 경우 에러가 발생한다.")
    @Test
    void settleVolunteerAppliesWhenNoAuthCenter() {
        // given
        UUID wrongId = UUID.randomUUID();

        Volunteer volunteer1 = volunteerRepository.save(createVolunteer());
        Volunteer volunteer2 = volunteerRepository.save(createVolunteer());

        RecruitBoard board = createCompletedRecruitBoard(UUID.randomUUID(), COUNSELING);
        recruitBoardRepository.save(board);

        VolunteerApply apply1 = createApply(volunteer1.getId(), board.getId());
        VolunteerApply apply2 = createApply(volunteer2.getId(), board.getId());
        volunteerApplyRepository.saveAll(List.of(apply1, apply2));

        VolunteerApplySettleRequestDto dto = VolunteerApplySettleRequestDto.builder()
                .ids(List.of(apply1.getId(), apply2.getId()))
                .build();

        // when
        // then
        assertThatThrownBy(
                () -> settleVolunteerApplyFacadeService.settleVolunteerApplies(dto, wrongId)
        ).isInstanceOf(BadRequestException.class)
                .hasMessage(UNAUTHORIZED_RECRUIT_BOARD.getMessage());
    }

    private static Volunteer createVolunteer() {
        return Volunteer.createDefault(NAVER, "naver");
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
