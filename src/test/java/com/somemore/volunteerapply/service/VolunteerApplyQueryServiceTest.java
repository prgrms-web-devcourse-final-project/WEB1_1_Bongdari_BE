package com.somemore.volunteerapply.service;

import static com.somemore.volunteerapply.domain.ApplyStatus.APPROVED;
import static com.somemore.volunteerapply.domain.ApplyStatus.REJECTED;
import static com.somemore.volunteerapply.domain.ApplyStatus.WAITING;
import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.support.IntegrationTestSupport;
import com.somemore.volunteerapply.domain.ApplyStatus;
import com.somemore.volunteerapply.domain.VolunteerApply;
import com.somemore.volunteerapply.dto.condition.VolunteerApplySearchCondition;
import com.somemore.volunteerapply.dto.response.VolunteerApplyResponseDto;
import com.somemore.volunteerapply.dto.response.VolunteerApplySummaryResponseDto;
import com.somemore.volunteerapply.repository.VolunteerApplyRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        VolunteerApply apply1 = createApply(volunteerId1, recruitId1);
        VolunteerApply apply2 = createApply(volunteerId2, recruitId2);

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

    @DisplayName("모집글 아이디로 지원 현황을 조회할 수 있다.")
    @Test
    void getSummaryByRecruitBoardId() {
        // given
        long approveCount = 10;
        long rejectCount = 2;
        long waitingCount = 5;
        long recruitBoardId = 100L;

        for (int i = 0; i < waitingCount; i++) {
            volunteerApplyRepository.save(createApply(UUID.randomUUID(), recruitBoardId, WAITING));
        }
        for (int i = 0; i < approveCount; i++) {
            volunteerApplyRepository.save(createApply(UUID.randomUUID(), recruitBoardId, APPROVED));
        }
        for (int i = 0; i < rejectCount; i++) {
            volunteerApplyRepository.save(createApply(UUID.randomUUID(), recruitBoardId, REJECTED));
        }

        // when
        VolunteerApplySummaryResponseDto dto = volunteerApplyQueryService.getSummaryByRecruitId(
                recruitBoardId);
        // then
        assertThat(dto.waiting()).isEqualTo(waitingCount);
        assertThat(dto.approve()).isEqualTo(approveCount);
        assertThat(dto.reject()).isEqualTo(rejectCount);
    }

    @DisplayName("모집글 아이디와 봉사자 아이디로 지원 응답 값을 조회할 수 있다.")
    @Test
    void getVolunteerApplyByRecruitIdAndVolunteerId() {
        // given
        Long recruitId = 1234L;
        UUID volunteerId = UUID.randomUUID();

        VolunteerApply newApply = createApply(volunteerId, recruitId);
        volunteerApplyRepository.save(newApply);

        // when
        VolunteerApplyResponseDto dto = volunteerApplyQueryService.getVolunteerApplyByRecruitIdAndVolunteerId(
                recruitId, volunteerId);

        // then
        assertThat(dto.recruitBoardId()).isEqualTo(recruitId);
        assertThat(dto.volunteerId()).isEqualTo(volunteerId);
    }

    @DisplayName("모집글 아이디로 지원 리스트를 페이징 조회할 수 있다.")
    @Test
    void getAllByRecruitId() {
        // given
        Long boardId = 1L;
        VolunteerApply apply1 = createApply(UUID.randomUUID(), boardId);
        VolunteerApply apply2 = createApply(UUID.randomUUID(), boardId);
        volunteerApplyRepository.saveAll(List.of(apply1, apply2));

        VolunteerApplySearchCondition condition = VolunteerApplySearchCondition.builder()
                .pageable(getPageable())
                .build();

        // when
        Page<VolunteerApply> applies = volunteerApplyQueryService.getAllByRecruitId(boardId,
                condition);

        // then
        assertThat(applies).hasSize(2);
    }

    @DisplayName("봉사자 아이디로 봉사 지원 리스트를 페이징 조회 할 수있다.")
    @Test
    void getVolunteerAppliesByVolunteerId() {
        // given
        UUID volunteerId = UUID.randomUUID();

        VolunteerApply apply1 = createApply(volunteerId, 200L);
        VolunteerApply apply2 = createApply(volunteerId, 201L);
        VolunteerApply apply3 = createApply(volunteerId, 202L);
        VolunteerApply apply4 = createApply(UUID.randomUUID(), 203L);
        volunteerApplyRepository.saveAll(List.of(apply1, apply2, apply3, apply4));

        VolunteerApplySearchCondition condition = VolunteerApplySearchCondition.builder()
                .pageable(getPageable())
                .build();

        // when
        Page<VolunteerApply> applies = volunteerApplyQueryService.getAllByVolunteerId(
                volunteerId, condition);

        // then
        assertThat(applies).hasSize(3);
    }

    @DisplayName("아이디 리스트로 봉사 지원 리스트를 조회할 수 있다.")
    @Test
    void getAllByIds() {
        // given
        Long recruitBoardId = 1L;

        VolunteerApply apply1 = volunteerApplyRepository.save(
                createApply(UUID.randomUUID(), recruitBoardId));
        VolunteerApply apply2 = volunteerApplyRepository.save(
                createApply(UUID.randomUUID(), recruitBoardId));
        VolunteerApply apply3 = volunteerApplyRepository.save(
                createApply(UUID.randomUUID(), recruitBoardId));
        VolunteerApply apply4 = volunteerApplyRepository.save(
                createApply(UUID.randomUUID(), recruitBoardId));

        List<Long> ids = List.of(apply1.getId(), apply2.getId(), apply3.getId(), apply4.getId());

        // when
        List<VolunteerApply> applies = volunteerApplyQueryService.getAllByIds(ids);

        // then
        assertThat(applies).hasSize(ids.size());
        assertThat(applies)
                .extracting(VolunteerApply::getId)
                .containsExactlyInAnyOrderElementsOf(ids);
    }

    private VolunteerApply createApply(UUID volunteerId, Long recruitId) {
        return createApply(volunteerId, recruitId, WAITING);
    }

    private VolunteerApply createApply(UUID volunteerId, Long recruitId, ApplyStatus status) {
        return VolunteerApply.builder()
                .volunteerId(volunteerId)
                .recruitBoardId(recruitId)
                .status(status)
                .attended(false)
                .build();
    }

    private Pageable getPageable() {
        return PageRequest.of(0, 4);
    }
}
