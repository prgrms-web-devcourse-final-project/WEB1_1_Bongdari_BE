package com.somemore.domains.volunteerapply.repository;

import static com.somemore.domains.volunteerapply.domain.ApplyStatus.APPROVED;
import static com.somemore.domains.volunteerapply.domain.ApplyStatus.REJECTED;
import static com.somemore.domains.volunteerapply.domain.ApplyStatus.WAITING;
import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.domains.volunteerapply.domain.ApplyStatus;
import com.somemore.domains.volunteerapply.domain.VolunteerApply;
import com.somemore.domains.volunteerapply.dto.condition.VolunteerApplySearchCondition;
import com.somemore.support.IntegrationTestSupport;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class VolunteerApplyRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private VolunteerApplyRepositoryImpl volunteerApplyRepository;

    @BeforeEach
    void setUp() {
        for (int i = 1; i <= 15; i++) {
            VolunteerApply apply = VolunteerApply.builder()
                    .volunteerId(UUID.randomUUID())
                    .recruitBoardId(1L)
                    .status(WAITING)
                    .attended(false)
                    .build();
            volunteerApplyRepository.save(apply);
        }

        for (int i = 1; i <= 5; i++) {
            VolunteerApply apply = VolunteerApply.builder()
                    .volunteerId(UUID.randomUUID())
                    .recruitBoardId(2L)
                    .status(APPROVED)
                    .attended(true)
                    .build();
            volunteerApplyRepository.save(apply);
        }
    }

    @DisplayName("봉사 신청 저장 및 조회")
    @Test
    void saveAndFindById() {
        // given
        VolunteerApply newApply = createApply(UUID.randomUUID(), 1L);
        volunteerApplyRepository.save(newApply);

        // when
        Optional<VolunteerApply> foundApply = volunteerApplyRepository.findById(newApply.getId());

        // then
        assertThat(foundApply).isPresent();
        assertThat(foundApply.get().getId()).isEqualTo(newApply.getId());
        assertThat(foundApply.get().getStatus()).isEqualTo(APPROVED);
    }

    @DisplayName("모집글 ID 리스트로 봉사자 ID 리스트 조회")
    @Test
    void findVolunteerIdsByRecruitIds() {
        // when
        List<UUID> volunteerIds = volunteerApplyRepository.findVolunteerIdsByRecruitIds(
                List.of(1L, 2L));

        // then
        assertThat(volunteerIds).hasSize(20);
    }

    @DisplayName("모집글 ID로 페이징된 봉사 신청 조회")
    @Test
    void findAllByRecruitId() {
        // given
        PageRequest firstPage = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("created_at")));
        PageRequest secondPage = PageRequest.of(1, 10, Sort.by(Sort.Order.asc("created_at")));

        // when
        Page<VolunteerApply> firstPageResult = volunteerApplyRepository.findAllByRecruitId(1L,
                firstPage);
        Page<VolunteerApply> secondPageResult = volunteerApplyRepository.findAllByRecruitId(1L,
                secondPage);

        // then
        assertThat(firstPageResult.getContent()).hasSize(10);
        assertThat(firstPageResult.getTotalElements()).isEqualTo(15);
        assertThat(firstPageResult.getTotalPages()).isEqualTo(2);
        assertThat(firstPageResult.hasNext()).isTrue();
        assertThat(firstPageResult.hasPrevious()).isFalse();

        assertThat(secondPageResult.getContent()).hasSize(5);
        assertThat(secondPageResult.hasNext()).isFalse();
        assertThat(secondPageResult.hasPrevious()).isTrue();
    }

    @DisplayName("모집글 아이디와 봉사자 아이디로 봉사 지원을 조회할 수 있다.")
    @Test
    void findByRecruitIdAndVolunteerId() {
        // given
        Long recruitId = 1234L;
        UUID volunteerId = UUID.randomUUID();

        VolunteerApply newApply = createApply(volunteerId, recruitId);
        volunteerApplyRepository.save(newApply);

        // when
        Optional<VolunteerApply> findApply = volunteerApplyRepository.findByRecruitIdAndVolunteerId(
                recruitId, volunteerId);

        // then
        assertThat(findApply).isPresent();
    }

    @DisplayName("아이디로 모집글아이디 조회할 수 있다.")
    @Test
    void findRecruitBoardIdById() {
        // given
        Long recruitId = 1234L;

        VolunteerApply newApply = createApply(UUID.randomUUID(), recruitId);
        volunteerApplyRepository.save(newApply);

        // when
        Optional<Long> findBoardId = volunteerApplyRepository.findRecruitBoardIdById(
                newApply.getId());

        // then
        assertThat(findBoardId).isPresent();
        assertThat(findBoardId.get()).isEqualTo(recruitId);
    }

    @DisplayName("모집글 아이디와 봉사자 아이디로 봉사 지원 존재 유무를 확인 할 수 있다.")
    @Test
    void existsByRecruitIdAndVolunteerId() {
        // given
        Long recruitId = 1234L;
        UUID volunteerId = UUID.randomUUID();

        VolunteerApply newApply = createApply(volunteerId, recruitId);
        volunteerApplyRepository.save(newApply);

        // when
        boolean result = volunteerApplyRepository.existsByRecruitIdAndVolunteerId(recruitId,
                volunteerId);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("모집글 아이디로 지원 리스트를 조회할 수 있다.")
    @Test
    void findAllByRecruitIdNotPaging() {
        // given
        Long recruitBoardId = 1L;

        // when
        List<VolunteerApply> applies = volunteerApplyRepository.findAllByRecruitId(recruitBoardId);

        // then
        assertThat(applies).hasSize(15);
    }

    @DisplayName("모집글 아이디와 조건 - 지원 상태, 참석여부으로 페이징 조회 할 수 있다.")
    @Test
    void findAllByRecruitIdWithCondition() {
        // given
        Long recruitBoardId = 101L;
        ApplyStatus status = APPROVED;
        Boolean attended = false;

        volunteerApplyRepository.save(createApply(recruitBoardId, status, attended));
        volunteerApplyRepository.save(createApply(recruitBoardId, status, attended));
        volunteerApplyRepository.save(createApply(recruitBoardId, status, attended));
        volunteerApplyRepository.save(createApply(recruitBoardId, REJECTED, !attended));

        VolunteerApplySearchCondition condition = VolunteerApplySearchCondition.builder()
                .status(status)
                .attended(attended)
                .pageable(getPageable())
                .build();

        // when
        Page<VolunteerApply> applies = volunteerApplyRepository.findAllByRecruitId(recruitBoardId,
                condition);

        // then
        assertThat(applies.getTotalElements()).isEqualTo(3);
        assertThat(applies.getContent())
                .allMatch(apply -> apply.getStatus() == status && apply.getAttended() == attended);

    }

    @DisplayName("봉사자 아이디와 조건 - 지원 상태, 참석 여부로 페이징 조회할 수 있다.")
    @Test
    void findAllByVolunteerId() {
        // given
        UUID volunteerId = UUID.randomUUID();
        ApplyStatus status = APPROVED;
        Boolean attended = false;

        volunteerApplyRepository.save(createApply(volunteerId, status, attended));
        volunteerApplyRepository.save(createApply(volunteerId, status, attended));
        volunteerApplyRepository.save(createApply(volunteerId, status, attended));
        volunteerApplyRepository.save(createApply(volunteerId, REJECTED, !attended));

        VolunteerApplySearchCondition condition = VolunteerApplySearchCondition.builder()
                .status(status)
                .attended(attended)
                .pageable(getPageable())
                .build();

        // when
        Page<VolunteerApply> applies = volunteerApplyRepository.findAllByVolunteerId(volunteerId,
                condition);

        // then
        assertThat(applies.getTotalElements()).isEqualTo(3);
        assertThat(applies.getContent())
                .allMatch(apply -> apply.getStatus() == status && apply.getAttended() == attended);

    }

    @DisplayName("아이디 리스트로 지원 리스트를 조회할 수 있다.")
    @Test
    void findAllByIds() {
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
        List<VolunteerApply> applies = volunteerApplyRepository.findAllByIds(ids);

        // then
        assertThat(applies).hasSize(ids.size());
        assertThat(applies)
                .extracting(VolunteerApply::getId)
                .containsExactlyInAnyOrderElementsOf(ids);

    }

    private static VolunteerApply createApply(UUID volunteerId, Long recruitId) {
        return VolunteerApply.builder()
                .volunteerId(volunteerId)
                .recruitBoardId(recruitId)
                .status(APPROVED)
                .attended(false)
                .build();
    }

    private static VolunteerApply createApply(Long recruitId, ApplyStatus status,
            Boolean attended) {
        return VolunteerApply.builder()
                .volunteerId(UUID.randomUUID())
                .recruitBoardId(recruitId)
                .status(status)
                .attended(attended)
                .build();
    }

    private static VolunteerApply createApply(UUID volunteerId, ApplyStatus status,
            Boolean attended) {
        return VolunteerApply.builder()
                .volunteerId(volunteerId)
                .recruitBoardId(101L)
                .status(status)
                .attended(attended)
                .build();
    }

    private Pageable getPageable() {
        return PageRequest.of(0, 10);
    }
}
