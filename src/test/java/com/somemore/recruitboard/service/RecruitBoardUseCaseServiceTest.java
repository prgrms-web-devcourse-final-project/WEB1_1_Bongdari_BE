package com.somemore.recruitboard.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.IntegrationTestSupport;
import com.somemore.location.dto.request.LocationCreateRequestDto;
import com.somemore.location.repository.LocationRepository;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.domain.VolunteerType;
import com.somemore.recruitboard.dto.request.RecruitCreateRequestDto;
import com.somemore.recruitboard.repository.RecruitBoardRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RecruitBoardUseCaseServiceTest extends IntegrationTestSupport {

    @Autowired
    private RecruitBoardUseCase recruitBoardUseCase;

    @Autowired
    private RecruitBoardRepository recruitBoardRepository;

    @Autowired
    private LocationRepository locationRepository;

    @AfterEach
    void tearDown() {
        recruitBoardRepository.deleteAllInBatch();
        locationRepository.deleteAllInBatch();
    }

    @DisplayName("봉사 모집글 생성 정보로 모집글을 저장한다")
    @Test
    void createRecruitBoardWithDto() {
        // given
        LocationCreateRequestDto locationDto = LocationCreateRequestDto.builder()
            .address("도로명 주소 33")
            .latitude("위도 정보")
            .longitude("경도 정보")
            .build();

        RecruitCreateRequestDto dto = RecruitCreateRequestDto.builder()
            .title("봉사 모집글 작성")
            .content("봉사 하실분을 모집합니다. <br>")
            .volunteerDate(LocalDateTime.now())
            .volunteerType(VolunteerType.OTHER)
            .volunteerHours(4)
            .admitted(true)
            .location(locationDto)
            .build();

        long centerId = 1L;
        Optional<String> imgUrl = Optional.of("https://image.domain.com/links");

        // when
        Long saveId = recruitBoardUseCase.createRecruitBoard(dto, centerId, imgUrl);
        Optional<RecruitBoard> recruitBoard = recruitBoardRepository.findById(saveId);

        // then
        assertThat(recruitBoard.isPresent()).isTrue();
        assertThat(recruitBoard.get().getId()).isEqualTo(saveId);
        assertThat(recruitBoard.get().getCenterId()).isEqualTo(centerId);
        assertThat(recruitBoard.get().getImgUrl()).isEqualTo(imgUrl.get());
    }

    @DisplayName("봉사 모집글 저장시 이미지 링크가 없을 경우 빈 문자열을 저장한다")
    @Test
    void createRecruitBoardWhenImgUrlIsEmpty() {
        // given
        LocationCreateRequestDto locationDto = LocationCreateRequestDto.builder()
            .address("도로명 주소 33")
            .latitude("위도 정보")
            .longitude("경도 정보")
            .build();

        RecruitCreateRequestDto dto = RecruitCreateRequestDto.builder()
            .title("봉사 모집글 작성")
            .content("봉사 하실분을 모집합니다. <br>")
            .volunteerDate(LocalDateTime.now())
            .volunteerType(VolunteerType.OTHER)
            .volunteerHours(4)
            .admitted(true)
            .location(locationDto)
            .build();

        long centerId = 1L;
        Optional<String> imgUrl = Optional.empty();

        // when
        Long saveId = recruitBoardUseCase.createRecruitBoard(dto, centerId, imgUrl);
        Optional<RecruitBoard> recruitBoard = recruitBoardRepository.findById(saveId);

        // then
        assertThat(recruitBoard.isPresent()).isTrue();
        assertThat(recruitBoard.get().getId()).isEqualTo(saveId);
        assertThat(recruitBoard.get().getCenterId()).isEqualTo(centerId);
        assertThat(recruitBoard.get().getImgUrl()).isEqualTo("");
    }
}