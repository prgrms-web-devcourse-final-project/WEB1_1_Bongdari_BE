package com.somemore.domains.volunteerapply.service;

import com.somemore.domains.volunteerapply.domain.ApplyStatus;
import com.somemore.domains.volunteerapply.domain.VolunteerApply;
import com.somemore.domains.volunteerapply.repository.VolunteerApplyRepository;
import com.somemore.global.exception.BadRequestException;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.somemore.domains.volunteerapply.domain.ApplyStatus.APPROVED;
import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_VOLUNTEER_APPLY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class WithdrawVolunteerApplyServiceTest extends IntegrationTestSupport {

    @Autowired
    private WithdrawVolunteerApplyService withdrawVolunteerApplyService;

    @Autowired
    private VolunteerApplyRepository volunteerApplyRepository;

    @DisplayName("봉사 지원을 철회할 수 있다.")
    @Test
    void withdraw() {
        // given
        UUID volunteerId = UUID.randomUUID();

        VolunteerApply apply = createApply(volunteerId, APPROVED, false);
        volunteerApplyRepository.save(apply);

        // when
        withdrawVolunteerApplyService.withdraw(apply.getId(), volunteerId);

        // then
        Optional<VolunteerApply> withdraw = volunteerApplyRepository.findById(apply.getId());

        assertThat(withdraw).isEmpty();
    }

    @DisplayName("지원한 본인이 아닐 경우 에러가 발생한다.")
    @Test
    void withdrawWhenWrongVolunteerId() {
        // given
        VolunteerApply apply = createApply(UUID.randomUUID(), APPROVED, false);
        volunteerApplyRepository.save(apply);

        UUID wrongId = UUID.randomUUID();
        Long applyId = apply.getId();
        // when
        // then
        assertThatThrownBy(
                () -> withdrawVolunteerApplyService.withdraw(applyId, wrongId)
        ).isInstanceOf(BadRequestException.class)
                .hasMessage(UNAUTHORIZED_VOLUNTEER_APPLY.getMessage());
    }

    private VolunteerApply createApply(UUID volunteerId, ApplyStatus status, boolean attended) {
        return VolunteerApply.builder()
                .volunteerId(volunteerId)
                .recruitBoardId(1L)
                .status(status)
                .attended(attended)
                .build();
    }
}
