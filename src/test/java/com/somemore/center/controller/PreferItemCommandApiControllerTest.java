package com.somemore.center.controller;

import com.somemore.ControllerTestSupport;
import com.somemore.WithMockCustomUser;
import com.somemore.center.dto.request.PreferItemCreateRequestDto;
import com.somemore.center.dto.response.PreferItemCreateResponseDto;
import com.somemore.center.usecase.command.CreatePreferItemUseCase;
import com.somemore.global.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PreferItemCommandApiControllerTest extends ControllerTestSupport {

    @MockBean
    private CreatePreferItemUseCase createPreferItemUseCase;


    @DisplayName("기관은 선호물품을 등록할 수 있다. (controller)")
    @Test
    @WithMockCustomUser(role = "CENTER")
    void registerPreferItem() throws Exception {
        // given
        UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"); // 고정된 UUID 사용
        PreferItemCreateRequestDto requestDto = new PreferItemCreateRequestDto("어린이 도서");
        PreferItemCreateResponseDto responseDto = new PreferItemCreateResponseDto(
                1L,
                userId,
                "어린이 도서"
        );

        given(createPreferItemUseCase.createPreferItem(userId, requestDto))
                .willReturn(responseDto);

        // when & then
        mockMvc.perform(post("/api/preferItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .principal(() -> userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("관심 기관 등록 성공"))
                .andExpect(jsonPath("$.data.id").value(responseDto.id()))
                .andExpect(jsonPath("$.data.center_id").value(responseDto.centerId().toString()))
                .andExpect(jsonPath("$.data.item_name").value(responseDto.itemName()));

        // verify
        verify(createPreferItemUseCase).createPreferItem(userId, requestDto);
    }

    @DisplayName("존재하지 않는 기관 ID로 선호물품을 등록할 수 없다. (controller)")
    @Test
    @WithMockCustomUser(role = "CENTER")
    void registerPreferItem_Fail_WhenCenterNotExists() throws Exception {
        // given
        UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        PreferItemCreateRequestDto requestDto = new PreferItemCreateRequestDto("어린이 도서");

        given(createPreferItemUseCase.createPreferItem(userId, requestDto))
                .willThrow(new BadRequestException("존재하지 않는 기관입니다."));

        // when & then
        mockMvc.perform(post("/api/preferItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .principal(() -> userId.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("존재하지 않는 기관입니다."));

        // verify
        verify(createPreferItemUseCase).createPreferItem(userId, requestDto);
    }
}