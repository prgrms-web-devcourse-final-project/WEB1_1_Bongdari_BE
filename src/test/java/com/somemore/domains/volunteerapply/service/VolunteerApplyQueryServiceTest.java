package com.somemore.domains.volunteerapply.service;

import com.somemore.domains.volunteerapply.domain.ApplyStatus;
import com.somemore.domains.volunteerapply.domain.VolunteerApply;
import com.somemore.domains.volunteerapply.dto.condition.VolunteerApplySearchCondition;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplyResponseDto;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplySummaryResponseDto;
import com.somemore.domains.volunteerapply.repository.VolunteerApplyRepository;
import com.somemore.global.exception.NoSuchElementException;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.somemore.domains.volunteerapply.domain.ApplyStatus.*;
import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_VOLUNTEER_APPLY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class VolunteerApplyQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private VolunteerApplyQueryService volunteerApplyQueryService;

    @Autowired
    private VolunteerApplyRepository volunteerApplyRepository;

    private Long recruitBoardId;
    private UUID volunteerId;
    private VolunteerApply apply;

    @BeforeEach
    void setUp() {
        recruitBoardId = 1234L;
        volunteerId = UUID.randomUUID();
        apply = createApply(volunteerId, recruitBoardId);
        volunteerApplyRepository.save(apply);
    }

    @DisplayName("봉사 지원 아이디로 조회할 수 있다.")
    @Test
    void getById() {
        // given
        Long id = apply.getId();

        // when
        VolunteerApply findApply = volunteerApplyQueryService.getById(id);

        // then
        assertThat(findApply.getId()).isEqualTo(id);
        assertThat(findApply.getVolunteerId()).isEqualTo(volunteerId);
        assertThat(findApply.getRecruitBoardId()).isEqualTo(recruitBoardId);
    }

    @DisplayName("존재하지 않는 봉사 지원 아이디로 조회할 경우 에러가 발생한다.")
    @Test
    void getByIdWhenDoesNotExist() {
        // given
        Long wrongId = 999L;

        // when
        // then
        assertThatThrownBy(
                () -> volunteerApplyQueryService.getById(wrongId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(NOT_EXISTS_VOLUNTEER_APPLY.getMessage());
    }

    @DisplayName("모집글 아이디와 봉사자 아이디로 조회할 수 있다")
    @Test
    void getByRecruitIdAndVolunteerId() {
        // given
        // when
        VolunteerApply findApply = volunteerApplyQueryService.getByRecruitIdAndVolunteerId(recruitBoardId, volunteerId);

        // then
        assertThat(findApply.getRecruitBoardId()).isEqualTo(recruitBoardId);
        assertThat(findApply.getVolunteerId()).isEqualTo(volunteerId);
    }

    @DisplayName("존재하지 않는 모집글 아이디와 봉사자 아이디로 조회할 수 있다")
    @Test
    void getByRecruitIdAndVolunteerIdWhenDoesNotExist() {
        // given
        Long wrongRecruitBoardId = 999L;
        UUID wrongVolunteerId = UUID.randomUUID();

        // when
        // then
        assertThatThrownBy(
                () -> volunteerApplyQueryService.getByRecruitIdAndVolunteerId(wrongRecruitBoardId, wrongVolunteerId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(NOT_EXISTS_VOLUNTEER_APPLY.getMessage());

    }

    @DisplayName("모집글 아이디로 지원 현황을 조회할 수 있다.")
    @Test
    void getSummaryByRecruitBoardId() {
        // given
        long approveCount = 10;
        long rejectCount = 2;
        long waitingCount = 5;
        long recruitId = 100L;

        for (int i = 0; i < waitingCount; i++) {
            volunteerApplyRepository.save(createApply(UUID.randomUUID(), recruitId, WAITING));
        }
        for (int i = 0; i < approveCount; i++) {
            volunteerApplyRepository.save(createApply(UUID.randomUUID(), recruitId, APPROVED));
        }
        for (int i = 0; i < rejectCount; i++) {
            volunteerApplyRepository.save(createApply(UUID.randomUUID(), recruitId, REJECTED));
        }

        // when
        VolunteerApplySummaryResponseDto dto = volunteerApplyQueryService.getSummaryByRecruitId(recruitId);

        // then
        assertThat(dto.waiting()).isEqualTo(waitingCount);
        assertThat(dto.approve()).isEqualTo(approveCount);
        assertThat(dto.reject()).isEqualTo(rejectCount);
    }

    @DisplayName("모집글 아이디와 봉사자 아이디로 지원 응답 값을 조회할 수 있다.")
    @Test
    void getVolunteerApplyByRecruitIdAndVolunteerId() {
        // given
        // when
        VolunteerApplyResponseDto dto = volunteerApplyQueryService.getVolunteerApplyByRecruitIdAndVolunteerId(
                recruitBoardId, volunteerId);

        // then
        assertThat(dto.recruitBoardId()).isEqualTo(recruitBoardId);
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
        Page<VolunteerApply> applies = volunteerApplyQueryService.getAllByRecruitId(boardId, condition);

        // then
        assertThat(applies).hasSize(2);
    }

    @DisplayName("봉사자 아이디로 봉사 지원 리스트를 페이징 조회 할 수있다.")
    @Test
    void getVolunteerAppliesByVolunteerId() {
        // given
        UUID volunteerOneId = UUID.randomUUID();

        VolunteerApply apply1 = createApply(volunteerOneId, 200L);
        VolunteerApply apply2 = createApply(volunteerOneId, 201L);
        VolunteerApply apply3 = createApply(volunteerOneId, 202L);
        VolunteerApply apply4 = createApply(UUID.randomUUID(), 203L);
        volunteerApplyRepository.saveAll(List.of(apply1, apply2, apply3, apply4));

        VolunteerApplySearchCondition condition = VolunteerApplySearchCondition.builder()
                .pageable(getPageable())
                .build();

        // when
        Page<VolunteerApply> applies = volunteerApplyQueryService.getAllByVolunteerId(
                volunteerOneId, condition);

        // then
        assertThat(applies).hasSize(3);
    }

    @DisplayName("아이디 리스트로 봉사 지원 리스트를 조회할 수 있다.")
    @Test
    void getAllByIds() {
        // given
        Long recruitId = 1L;

        VolunteerApply apply1 = volunteerApplyRepository.save(createApply(UUID.randomUUID(), recruitId));
        VolunteerApply apply2 = volunteerApplyRepository.save(createApply(UUID.randomUUID(), recruitId));
        VolunteerApply apply3 = volunteerApplyRepository.save(createApply(UUID.randomUUID(), recruitId));
        VolunteerApply apply4 = volunteerApplyRepository.save(createApply(UUID.randomUUID(), recruitId));

        List<Long> ids = List.of(apply1.getId(), apply2.getId(), apply3.getId(), apply4.getId());

        // when
        List<VolunteerApply> applies = volunteerApplyQueryService.getAllByIds(ids);

        // then
        assertThat(applies).hasSize(ids.size());
        assertThat(applies)
                .extracting(VolunteerApply::getId)
                .containsExactlyInAnyOrderElementsOf(ids);
    }

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

    @DisplayName("존재하지 않는 봉사 지원을 조회할 경우 에러가 발생한다.")
    @Test
    void getByRecruitIdAndVolunteerIdWhenNotExist() {
        // given
        Long wrongBoardId = 999L;
        UUID wrongVolunteerId = UUID.randomUUID();

        // when
        // then
        assertThatThrownBy(
                () -> volunteerApplyQueryService.getByRecruitIdAndVolunteerId(wrongBoardId,
                        wrongVolunteerId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(NOT_EXISTS_VOLUNTEER_APPLY.getMessage());
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
