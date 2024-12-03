package com.somemore.volunteerapply.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.IntegrationTestSupport;
import com.somemore.volunteerapply.domain.ApplyStatus;
import com.somemore.volunteerapply.domain.VolunteerApply;
import com.somemore.volunteerapply.repository.VolunteerApplyRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class VolunteerApplyQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private VolunteerApplyQueryService volunteerApplyQueryService;

    @Autowired
    private VolunteerApplyRepository volunteerApplyRepository;

    @DisplayName("recruitIds로 봉사자 ID 리스트를 조회할 수 있다")
    @Test
    void getVolunteerIdsByRecruitIds() {
        // Given
        Long recruitId1 = 10L;
        Long recruitId2 = 20L;
        UUID volunteerId1 = UUID.randomUUID();
        UUID volunteerId2 = UUID.randomUUID();

        VolunteerApply apply1 = createVolunteerApply(recruitId1, volunteerId1);
        VolunteerApply apply2 = createVolunteerApply(recruitId2, volunteerId2);

        volunteerApplyRepository.save(apply1);
        volunteerApplyRepository.save(apply2);

        // When
        List<UUID> volunteerIds = volunteerApplyQueryService.getVolunteerIdsByRecruitIds(
                List.of(recruitId1, recruitId2));

        // Then
        assertThat(volunteerIds)
                .hasSize(2)
                .containsExactlyInAnyOrder(volunteerId1, volunteerId2);
    }

    private VolunteerApply createVolunteerApply(Long recruitId, UUID volunteerId) {
        return VolunteerApply.builder()
                .volunteerId(volunteerId)
                .recruitBoardId(recruitId)
                .status(ApplyStatus.WAITING)
                .attended(false)
                .build();
    }

    @DisplayName("모집글 아이디와 봉사자 아이디로 조회할 수 있다")
    @Test
    void getByRecruitIdAndVolunteerId() {
        // given
        Long recruitId = 1234L;
        UUID volunteerId = UUID.randomUUID();

        VolunteerApply newApply = createApply(volunteerId, recruitId);
        volunteerApplyRepository.save(newApply);

        // when
        VolunteerApply apply = volunteerApplyQueryService.getByRecruitIdAndVolunteerId(
                recruitId, volunteerId);

        // then
        assertThat(apply.getRecruitBoardId()).isEqualTo(recruitId);
        assertThat(apply.getVolunteerId()).isEqualTo(volunteerId);
    }

    private static VolunteerApply createApply(UUID volunteerId, Long recruitId) {
        return VolunteerApply.builder()
                .volunteerId(volunteerId)
                .recruitBoardId(recruitId)
                .status(ApplyStatus.APPROVED)
                .attended(false)
                .build();
    }
}
