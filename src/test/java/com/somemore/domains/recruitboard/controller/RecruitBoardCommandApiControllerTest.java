package com.somemore.domains.recruitboard.controller;

import static com.somemore.domains.recruitboard.domain.RecruitStatus.CLOSED;
import static com.somemore.domains.recruitboard.domain.VolunteerCategory.OTHER;
import static com.somemore.support.fixture.LocalDateTimeFixture.createStartDateTime;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.somemore.domains.location.dto.request.LocationCreateRequestDto;
import com.somemore.domains.recruitboard.domain.RecruitStatus;
import com.somemore.domains.recruitboard.dto.request.RecruitBoardCreateRequestDto;
import com.somemore.domains.recruitboard.dto.request.RecruitBoardLocationUpdateRequestDto;
import com.somemore.domains.recruitboard.dto.request.RecruitBoardStatusUpdateRequestDto;
import com.somemore.domains.recruitboard.dto.request.RecruitBoardUpdateRequestDto;
import com.somemore.domains.recruitboard.usecase.CreateRecruitBoardUseCase;
import com.somemore.domains.recruitboard.usecase.DeleteRecruitBoardUseCase;
import com.somemore.domains.recruitboard.usecase.UpdateRecruitBoardUseCase;
import com.somemore.support.ControllerTestSupport;
import com.somemore.support.annotation.MockUser;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

class RecruitBoardCommandApiControllerTest extends ControllerTestSupport {

    @MockBean
    private CreateRecruitBoardUseCase createRecruitBoardUseCase;

    @MockBean
    private UpdateRecruitBoardUseCase updateRecruitBoardUseCase;

    @MockBean
    private DeleteRecruitBoardUseCase deleteRecruitBoardUseCase;


    @Test
    @DisplayName("봉사 활동 모집글 등록 성공 테스트")
    @MockUser(role = "ROLE_CENTER")
    void createRecruitBoard_success() throws Exception {
        // given
        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(2);

        LocationCreateRequestDto location = LocationCreateRequestDto.builder()
                .address("위치위치")
                .latitude(BigDecimal.valueOf(37.4845373748015))
                .longitude(BigDecimal.valueOf(127.010842267696))
                .build();

        RecruitBoardCreateRequestDto requestDto = RecruitBoardCreateRequestDto.builder()
                .title("봉사 모집글 작성")
                .content("봉사 하실분을 모집합니다. <br>")
                .region("지역")
                .recruitmentCount(10)
                .volunteerStartDateTime(startDateTime)
                .volunteerEndDateTime(endDateTime)
                .volunteerHours(2)
                .volunteerCategory(OTHER)
                .admitted(true)
                .location(location)
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDto);
        long boardId = 1L;

        given(createRecruitBoardUseCase.createRecruitBoard(any(), any(UUID.class)))
                .willReturn(boardId);

        // when
        mockMvc.perform(post("/api/recruit-board")
                        .content(requestBody)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer access-token"))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.data").value(boardId))
                .andExpect(jsonPath("$.message").value("봉사 활동 모집글 등록 성공"));
    }

    @DisplayName("봉사 활동 모집글 수정 성공 테스트")
    @Test
    @MockUser(role = "ROLE_CENTER")
    void updateRecruitBoard() throws Exception {
        // given
        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(2);

        RecruitBoardUpdateRequestDto requestDto = RecruitBoardUpdateRequestDto.builder()
                .title("서울 청계천 환경 미화 봉사 모집")
                .content("서울 청계천 주변 환경 미화 봉사 모집합니다. <br>")
                .region("서울 특별시")
                .recruitmentCount(10)
                .volunteerStartDateTime(startDateTime)
                .volunteerEndDateTime(endDateTime)
                .volunteerHours(2)
                .volunteerCategory(OTHER)
                .admitted(true)
                .build();

        willDoNothing().given(updateRecruitBoardUseCase)
                .updateRecruitBoard(any(), any(), any(UUID.class));

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        mockMvc.perform(put("/api/recruit-board/{id}", 1)
                        .content(requestBody)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer access-token"))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("봉사 활동 모집글 수정 성공"));
    }

    @DisplayName("봉사 활동 모집글 위치 수정 성공 테스트")
    @Test
    @MockUser(role = "ROLE_CENTER")
    void updateRecruitBoardLocation() throws Exception {
        // given
        RecruitBoardLocationUpdateRequestDto requestDto = RecruitBoardLocationUpdateRequestDto.builder()
                .region("새로새로지역지역")
                .address("새로새로주소주소")
                .latitude(BigDecimal.valueOf(37.2222222))
                .longitude(BigDecimal.valueOf(127.2222222))
                .build();

        willDoNothing().given(updateRecruitBoardUseCase)
                .updateRecruitBoardLocation(any(), any(), any(UUID.class));

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        mockMvc.perform(put("/api/recruit-board/{id}/location", 1L)
                        .content(requestBody)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer access-token"))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("봉사 활동 모집글 위치 수정 성공"));
    }

    @DisplayName("봉사 활동 상태 변경 성공")
    @Test
    @MockUser(role = "ROLE_CENTER")
    void updateRecruitBoardStatus() throws Exception {
        // given
        RecruitStatus status = CLOSED;
        RecruitBoardStatusUpdateRequestDto dto = new RecruitBoardStatusUpdateRequestDto(
                status);
        String requestBody = objectMapper.writeValueAsString(dto);
        willDoNothing().given(updateRecruitBoardUseCase)
                .updateRecruitBoardStatus(any(), any(), any(UUID.class));

        // when
        mockMvc.perform(patch("/api/recruit-board/{id}", 1L)
                        .content(requestBody)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer access-token"))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("봉사 활동 모집글 상태 수정 성공"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("봉사 활동 모집글 삭제 성공")
    @Test
    @MockUser(role = "ROLE_CENTER")
    void deleteRecruitBoard() throws Exception {
        // given
        Long recruitBoardId = 1L;
        willDoNothing().given(deleteRecruitBoardUseCase).deleteRecruitBoard(any(UUID.class), any());

        // when
        mockMvc.perform(delete("/api/recruit-board/{id}", recruitBoardId)
                        .header("Authorization", "Bearer access-token"))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("봉사 활동 모집글 삭제 성공"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}

