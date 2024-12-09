package com.somemore.interestcenter.controller;

import com.somemore.ControllerTestSupport;
import com.somemore.WithMockCustomUser;
import com.somemore.interestcenter.dto.request.RegisterInterestCenterRequestDto;
import com.somemore.interestcenter.dto.response.RegisterInterestCenterResponseDto;
import com.somemore.interestcenter.usecase.CancelInterestCenterUseCase;
import com.somemore.interestcenter.usecase.RegisterInterestCenterUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.UUID;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class InterestCenterCommandApiControllerTest extends ControllerTestSupport {

    @MockBean
    private RegisterInterestCenterUseCase registerInterestCenterUseCase;

    @MockBean
    private CancelInterestCenterUseCase cancelInterestCenterUseCase;

    private RegisterInterestCenterRequestDto requestDto;

    @BeforeEach
    void setUp() {
        requestDto = new RegisterInterestCenterRequestDto(
                UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
        );
    }

    @WithMockCustomUser
    @Test
    void registerInterestCenter_ShouldReturnSuccess() throws Exception {
        // given
        UUID volunteerId = UUID.randomUUID();

        RegisterInterestCenterResponseDto responseDto = new RegisterInterestCenterResponseDto(1L, volunteerId, requestDto.centerId());
        given(registerInterestCenterUseCase.registerInterestCenter(any(UUID.class), any(RegisterInterestCenterRequestDto.class)))
                .willReturn(responseDto);

        // when & then
        mockMvc.perform(post("/api/interest-center")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("관심 기관 등록 성공"))
                .andExpect(jsonPath("$.data.id").value(responseDto.id()))
                .andExpect(jsonPath("$.data.volunteer_id").value(responseDto.volunteerId().toString()))
                .andExpect(jsonPath("$.data.center_id").value(responseDto.centerId().toString()));
    }

    @WithMockCustomUser
    @Test
    void deleteInterestCenter_ShouldReturnSuccess() throws Exception {
        // given
        UUID centerId = UUID.randomUUID();
        doNothing().when(cancelInterestCenterUseCase).cancelInterestCenter(any(UUID.class), any(UUID.class));

        // when & then
        mockMvc.perform(delete("/api/interest-center/{centerId}", centerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("관심 기관 취소 성공"));
    }
}
