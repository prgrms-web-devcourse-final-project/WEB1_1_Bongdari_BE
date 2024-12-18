package com.somemore.domains.interestcenter.controller;

import com.somemore.domains.interestcenter.dto.response.InterestCentersResponseDto;
import com.somemore.domains.interestcenter.usecase.InterestCenterQueryUseCase;
import com.somemore.support.ControllerTestSupport;
import com.somemore.support.annotation.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InterestCenterQueryApiControllerTest extends ControllerTestSupport {

    @MockBean
    private InterestCenterQueryUseCase interestCenterQueryUseCase;

    @DisplayName("봉사자 ID로 관심기관 목록을 조회할 수 있다.")
    @Test
    @WithMockCustomUser
    void getInterestCenters_ShouldReturnInterestCentersList() throws Exception {
        // given
        UUID volunteerId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID centerId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        List<InterestCentersResponseDto> responseDtos = List.of(
                new InterestCentersResponseDto(centerId, "센터1", "http://image1.jpg"),
                new InterestCentersResponseDto(centerId, "센터2", "http://image2.jpg")
        );

        given(interestCenterQueryUseCase.getInterestCenters(volunteerId)).willReturn(responseDtos);

        // when & then
        mockMvc.perform(get("/api/interest-centers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("관심기관 조회 성공"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].center_name").value("센터1"))
                .andExpect(jsonPath("$.data[0].img_url").value("http://image1.jpg"))
                .andExpect(jsonPath("$.data[1].center_name").value("센터2"))
                .andExpect(jsonPath("$.data[1].img_url").value("http://image2.jpg"));
    }


    @DisplayName("봉사자 ID로 관심기관이 없을 경우 빈 리스트를 반환한다.")
    @Test
    @WithMockCustomUser
    void getInterestCenters_ShouldReturnEmptyList_WhenNoInterestCenters() throws Exception {
        // given
        UUID volunteerId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        given(interestCenterQueryUseCase.getInterestCenters(volunteerId)).willReturn(List.of());

        // when & then
        mockMvc.perform(get("/api/interest-centers")
                        .param("volunteerId", volunteerId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("관심기관 조회 성공"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}

