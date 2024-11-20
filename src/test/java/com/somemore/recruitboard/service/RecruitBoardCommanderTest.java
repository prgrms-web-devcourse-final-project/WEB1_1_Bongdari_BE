package com.somemore.recruitboard.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.IntegrationTestSupport;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.domain.VolunteerType;
import com.somemore.recruitboard.repository.RecruitBoardRepository;
import com.somemore.recruitboard.dto.command.RecruitBoardCreateCommandRequestDto;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RecruitBoardCommanderTest extends IntegrationTestSupport {

    @Autowired
    private RecruitBoardCommander recruitBoardCommander;

    @Autowired
    private RecruitBoardRepository recruitBoardRepository;


    @AfterEach
    void tearDown() {
        recruitBoardRepository.deleteAllInBatch();
    }

    @DisplayName("모집글 생성 정보를 받아 봉사 모집글을 저장한다.")
    @Test
    void createRecruitBoardWithCreateRequestDto() {
        // given
        RecruitBoardCreateCommandRequestDto requestDto = RecruitBoardCreateCommandRequestDto.builder()
            .centerId(1L)
            .locationId(1L)
            .imgUrl("https://image.domain.com/links")
            .title("봉사 모집글 작성")
            .content("봉사 하실분을 모집합니다. <br>")
            .volunteerDate(LocalDateTime.now())
            .volunteerType(VolunteerType.OTHER)
            .volunteerHours(4)
            .admitted(true)
            .build();

        // when
        Long saveId = recruitBoardCommander.create(requestDto);
        Optional<RecruitBoard> recruitBoard = recruitBoardRepository.findById(saveId);

        // then
        assertThat(recruitBoard.isPresent()).isTrue();
        assertThat(recruitBoard.get().getId()).isEqualTo(saveId);
        assertThat(recruitBoard.get().getCenterId()).isEqualTo(requestDto.centerId());
        assertThat(recruitBoard.get().getLocationId()).isEqualTo(requestDto.locationId());
        assertThat(recruitBoard.get().getImgUrl()).isEqualTo(requestDto.imgUrl());
    }


}