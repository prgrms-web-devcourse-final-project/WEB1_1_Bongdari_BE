package com.somemore.volunteerapply.service;

import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_VOLUNTEER_APPLY;
import static com.somemore.volunteerapply.domain.ApplyStatus.APPROVED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.somemore.IntegrationTestSupport;
import com.somemore.global.exception.BadRequestException;
import com.somemore.volunteerapply.domain.ApplyStatus;
import com.somemore.volunteerapply.domain.VolunteerApply;
import com.somemore.volunteerapply.repository.VolunteerApplyRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        UUID wrongId = UUID.randomUUID();

        VolunteerApply apply = createApply(UUID.randomUUID(), APPROVED, false);
        volunteerApplyRepository.save(apply);

        // when
        // then
        assertThatThrownBy(
                () -> withdrawVolunteerApplyService.withdraw(apply.getId(), wrongId)
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
