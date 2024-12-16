package com.somemore.recruitboard.controller;

import static com.somemore.support.fixture.LocalDateTimeFixture.createStartDateTime;
import static com.somemore.recruitboard.domain.RecruitStatus.CLOSED;
import static com.somemore.recruitboard.domain.VolunteerCategory.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.somemore.support.ControllerTestSupport;
import com.somemore.support.annotation.WithMockCustomUser;
import com.somemore.global.imageupload.usecase.ImageUploadUseCase;
import com.somemore.location.dto.request.LocationCreateRequestDto;
import com.somemore.recruitboard.domain.RecruitStatus;
import com.somemore.recruitboard.dto.request.RecruitBoardCreateRequestDto;
import com.somemore.recruitboard.dto.request.RecruitBoardLocationUpdateRequestDto;
import com.somemore.recruitboard.dto.request.RecruitBoardStatusUpdateRequestDto;
import com.somemore.recruitboard.dto.request.RecruitBoardUpdateRequestDto;
import com.somemore.recruitboard.usecase.command.CreateRecruitBoardUseCase;
import com.somemore.recruitboard.usecase.command.DeleteRecruitBoardUseCase;
import com.somemore.recruitboard.usecase.command.UpdateRecruitBoardUseCase;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

class RecruitBoardCommandApiControllerTest extends ControllerTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateRecruitBoardUseCase createRecruitBoardUseCase;

    @MockBean
    private UpdateRecruitBoardUseCase updateRecruitBoardUseCase;

    @MockBean
    private DeleteRecruitBoardUseCase deleteRecruitBoardUseCase;

    @MockBean
    private ImageUploadUseCase imageUploadUseCase;

    @Test
    @DisplayName("봉사 활동 모집글 등록 성공 테스트")
    @WithMockCustomUser(role = "CENTER")
    void createRecruitBoard_success() throws Exception {
        // given
        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(2);

        LocationCreateRequestDto location = LocationCreateRequestDto.builder()
                .address("위치위치")
                .latitude(BigDecimal.valueOf(37.4845373748015))
                .longitude(BigDecimal.valueOf(127.010842267696))
                .build();

        RecruitBoardCreateRequestDto dto = RecruitBoardCreateRequestDto.builder()
                .title("봉사 모집글 작성")
                .content("봉사 하실분을 모집합니다. <br>")
                .region("지역")
                .recruitmentCount(10)
                .volunteerStartDateTime(startDateTime)
                .volunteerEndDateTime(endDateTime)
                .volunteerCategory(OTHER)
                .admitted(true)
                .location(location)
                .build();

        MockMultipartFile imageFile = new MockMultipartFile(
                "img_file",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        MockMultipartFile requestData = new MockMultipartFile(
                "data",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(dto)
        );

        String mockImageUrl = "http://example.com/image/test-image.jpg";
        long mockRecruitBoardId = 1L;

        given(imageUploadUseCase.uploadImage(any())).willReturn(mockImageUrl);
        given(createRecruitBoardUseCase.createRecruitBoard(any(), any(UUID.class),
                anyString())).willReturn(mockRecruitBoardId);

        // when
        mockMvc.perform(multipart("/api/recruit-board")
                        .file(requestData)
                        .file(imageFile)
                        .contentType(MULTIPART_FORM_DATA)
                        .header("Authorization", "Bearer access-token"))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.data").value(mockRecruitBoardId))
                .andExpect(jsonPath("$.message").value("봉사 활동 모집글 등록 성공"));
    }

    @DisplayName("봉사 활동 모집글 수정 성공 테스트")
    @Test
    @WithMockCustomUser(role = "CENTER")
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
                .volunteerCategory(OTHER)
                .admitted(true)
                .build();

        MockMultipartFile imageFile = new MockMultipartFile(
                "img_file",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        MockMultipartFile requestData = new MockMultipartFile(
                "data",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(requestDto)
        );

        String mockImageUrl = "http://example.com/image/test-image.jpg";

        given(imageUploadUseCase.uploadImage(any())).willReturn(mockImageUrl);
        willDoNothing().given(updateRecruitBoardUseCase)
                .updateRecruitBoard(any(), any(), any(UUID.class), anyString());

        MockMultipartHttpServletRequestBuilder builder = multipart("/api/recruit-board/{id}", 1);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        // when
        mockMvc.perform(builder
                        .file(requestData)
                        .file(imageFile)
                        .contentType(MULTIPART_FORM_DATA)
                        .header("Authorization", "Bearer access-token"))
                //then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("봉사 활동 모집글 수정 성공"));
    }

    @DisplayName("봉사 활동 모집글 위치 수정 성공 테스트")
    @Test
    @WithMockCustomUser(role = "CENTER")
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer access-token"))
                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("봉사 활동 모집글 위치 수정 성공"));
    }

    @DisplayName("봉사 활동 상태 변경 성공")
    @Test
    @WithMockCustomUser(role = "CENTER")
    void updateRecruitBoardStatus() throws Exception {
        // given
        RecruitStatus status = CLOSED;
        RecruitBoardStatusUpdateRequestDto dto = new RecruitBoardStatusUpdateRequestDto(
                status);
        String requestBody = objectMapper.writeValueAsString(dto);
        willDoNothing().given(updateRecruitBoardUseCase)
                .updateRecruitBoardStatus(any(), any(), any(UUID.class), any(LocalDateTime.class));

        // when
        mockMvc.perform(patch("/api/recruit-board/{id}", 1L)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer access-token"))
                //then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("봉사 활동 모집글 상태 수정 성공"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("봉사 활동 모집글 삭제 성공")
    @Test
    @WithMockCustomUser(role = "CENTER")
    void deleteRecruitBoard() throws Exception {
        // given
        Long recruitBoardId = 1L;
        willDoNothing().given(deleteRecruitBoardUseCase).deleteRecruitBoard(any(UUID.class), any());

        // when
        mockMvc.perform(delete("/api/recruit-board/{id}", recruitBoardId)
                        .header("Authorization", "Bearer access-token"))
                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("봉사 활동 모집글 삭제 성공"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}

