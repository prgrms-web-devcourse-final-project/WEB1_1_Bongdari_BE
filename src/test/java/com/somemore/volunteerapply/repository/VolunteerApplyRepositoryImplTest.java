package com.somemore.volunteerapply.repository;


import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.IntegrationTestSupport;
import com.somemore.volunteerapply.domain.ApplyStatus;
import com.somemore.volunteerapply.domain.VolunteerApply;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class VolunteerApplyRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private VolunteerApplyRepositoryImpl volunteerApplyRepository;

    @BeforeEach
    void setUp() {
        // Given
        for (int i = 1; i <= 15; i++) {
            VolunteerApply apply = VolunteerApply.builder()
                    .volunteerId(UUID.randomUUID())
                    .recruitBoardId(1L)
                    .status(ApplyStatus.WAITING)
                    .attended(false)
                    .build();
            volunteerApplyRepository.save(apply);
        }

        for (int i = 1; i <= 5; i++) {
            VolunteerApply apply = VolunteerApply.builder()
                    .volunteerId(UUID.randomUUID())
                    .recruitBoardId(2L)
                    .status(ApplyStatus.APPROVED)
                    .attended(true)
                    .build();
            volunteerApplyRepository.save(apply);
        }
    }

    @DisplayName("봉사 신청 저장 및 조회")
    @Test
    void saveAndFindById() {
        // Given
        VolunteerApply newApply = VolunteerApply.builder()
                .volunteerId(UUID.randomUUID())
                .recruitBoardId(1L)
                .status(ApplyStatus.APPROVED)
                .attended(false)
                .build();
        VolunteerApply savedApply = volunteerApplyRepository.save(newApply);

        // When
        Optional<VolunteerApply> foundApply = volunteerApplyRepository.findById(savedApply.getId());

        // Then
        assertThat(foundApply).isPresent();
        assertThat(foundApply.get().getId()).isEqualTo(savedApply.getId());
        assertThat(foundApply.get().getStatus()).isEqualTo(ApplyStatus.APPROVED);
    }

    @DisplayName("모집글 ID 리스트로 봉사자 ID 리스트 조회")
    @Test
    void findVolunteerIdsByRecruitIds() {
        // When
        List<UUID> volunteerIds = volunteerApplyRepository.findVolunteerIdsByRecruitIds(
                List.of(1L, 2L));

        // Then
        assertThat(volunteerIds).hasSize(20);
    }

    @DisplayName("모집글 ID로 페이징된 봉사 신청 조회")
    @Test
    void findAllByRecruitId() {
        // Given
        PageRequest firstPage = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("created_at")));
        PageRequest secondPage = PageRequest.of(1, 10, Sort.by(Sort.Order.asc("created_at")));

        // When
        Page<VolunteerApply> firstPageResult = volunteerApplyRepository.findAllByRecruitId(1L,
                firstPage);
        Page<VolunteerApply> secondPageResult = volunteerApplyRepository.findAllByRecruitId(1L,
                secondPage);

        // Then
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

        VolunteerApply newApply = VolunteerApply.builder()
                .volunteerId(volunteerId)
                .recruitBoardId(recruitId)
                .status(ApplyStatus.APPROVED)
                .attended(false)
                .build();
        volunteerApplyRepository.save(newApply);

        // when
        Optional<VolunteerApply> findApply = volunteerApplyRepository.findByRecruitIdAndVolunteerId(
                recruitId, volunteerId);

        // then
        assertThat(findApply).isPresent();
    }
}
