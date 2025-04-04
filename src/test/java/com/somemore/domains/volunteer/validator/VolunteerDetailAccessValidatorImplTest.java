package com.somemore.domains.volunteer.validator;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.repository.RecruitBoardRepository;
import com.somemore.domains.volunteerapply.domain.ApplyStatus;
import com.somemore.domains.volunteerapply.domain.VolunteerApply;
import com.somemore.domains.volunteerapply.repository.VolunteerApplyRepository;
import com.somemore.global.exception.BadRequestException;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_VOLUNTEER_DETAIL;
import static com.somemore.support.fixture.RecruitBoardFixture.createRecruitBoard;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class VolunteerDetailAccessValidatorImplTest extends IntegrationTestSupport {

    @Autowired
    private VolunteerDetailAccessValidatorImpl volunteerDetailAccessValidatorImpl;

    @Autowired
    private RecruitBoardRepository recruitBoardRepository;

    @Autowired
    private VolunteerApplyRepository volunteerApplyRepository;

    @DisplayName("타겟 봉사자가 모집 글과 연관된 경우 검증 통과, 연관되지 않은 경우 예외를 던진다")
    @Test
    void validateByCenterIdThrowsExceptionWhenVolunteerNotLinked() {
        // given
        UUID centerId = UUID.randomUUID();

        RecruitBoard recruitBoard = createRecruitBoard(centerId);
        recruitBoardRepository.save(recruitBoard);

        UUID unrelatedVolunteerId = UUID.randomUUID();
        UUID relatedVolunteerId = UUID.randomUUID();

        VolunteerApply volunteerApply = createVolunteerApply(recruitBoard.getId(),
                relatedVolunteerId);
        volunteerApplyRepository.save(volunteerApply);

        // when
        // then
        assertThatCode(() -> volunteerDetailAccessValidatorImpl.validateByCenterId(centerId,
                relatedVolunteerId))
                .doesNotThrowAnyException();

        assertThatThrownBy(() -> volunteerDetailAccessValidatorImpl.validateByCenterId(centerId,
                unrelatedVolunteerId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(UNAUTHORIZED_VOLUNTEER_DETAIL.getMessage());
    }

    private VolunteerApply createVolunteerApply(Long recruitId, UUID volunteerId) {
        return VolunteerApply.builder()
                .recruitBoardId(recruitId)
                .volunteerId(volunteerId)
                .status(ApplyStatus.WAITING)
                .attended(false)
                .build();
    }
}
