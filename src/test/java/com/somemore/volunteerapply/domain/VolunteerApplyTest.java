package com.somemore.volunteerapply.domain;

import static com.somemore.volunteerapply.domain.ApplyStatus.APPROVED;
import static com.somemore.volunteerapply.domain.ApplyStatus.REJECTED;
import static com.somemore.volunteerapply.domain.ApplyStatus.WAITING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VolunteerApplyTest {

    @DisplayName("지원 상태를 변경할 수 있다")
    @Test
    void changeStatus() {
        // given
        VolunteerApply apply = createApply(UUID.randomUUID(), WAITING, false);

        // when
        apply.changeStatus(APPROVED);

        // then
        assertThat(apply.getStatus()).isEqualTo(APPROVED);
    }

    @Test
    @DisplayName("지원 상태 변경 실패 - 이미 완료된 봉사활동")
    void changeStatusWhenCompletedActivity() {
        // given
        VolunteerApply apply = createApply(UUID.randomUUID(), APPROVED, true);

        // when
        // then
        assertThatThrownBy(
                () -> apply.changeStatus(REJECTED)
        ).isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("봉사 지원 상태를 변경")
    @Test
    void markAsAttended() {
        // given
        VolunteerApply volunteerApply = createApply(UUID.randomUUID(), APPROVED, false);

        // when
        volunteerApply.changeAttended(true);

        // then
        assertThat(volunteerApply.getAttended()).isTrue();
    }

    @Test
    @DisplayName("참석 처리 실패 - 승인되지 않은 상태")
    void changeAttendedWhenNotApproved() {
        // given
        VolunteerApply apply = createApply(UUID.randomUUID(), WAITING, false);

        // when & then
        assertThatThrownBy(
                () -> apply.changeAttended(true)
        ).isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("봉사 참석 여부 확인")
    @Test
    void isCompleted() {
        // given
        VolunteerApply apply = createApply(UUID.randomUUID(), APPROVED, true);

        // when
        boolean res = apply.isVolunteerActivityCompleted();

        // then
        assertThat(res).isTrue();
    }

    @DisplayName("지원자 아이디로 본인이 작성한 지원인지 확인할 수있다.")
    @Test
    void isOwnApplication() {
        // given
        UUID volunteerId = UUID.randomUUID();
        VolunteerApply apply = createApply(volunteerId, APPROVED, false);

        // when
        boolean result = apply.isOwnApplication(volunteerId);

        // then
        assertThat(result).isTrue();
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
