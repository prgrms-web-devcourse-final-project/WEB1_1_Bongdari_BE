package com.somemore.domains.center.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.somemore.domains.center.dto.request.PreferItemCreateRequestDto;
import com.somemore.domains.center.dto.response.PreferItemCreateResponseDto;
import com.somemore.domains.center.usecase.command.CreatePreferItemUseCase;
import com.somemore.domains.center.usecase.command.DeletePreferItemUseCase;
import com.somemore.global.exception.BadRequestException;
import com.somemore.support.ControllerTestSupport;
import com.somemore.support.annotation.MockUser;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

class PreferItemCommandApiControllerTest extends ControllerTestSupport {

    @MockBean
    private CreatePreferItemUseCase createPreferItemUseCase;

    @MockBean
    private DeletePreferItemUseCase deletePreferItemUseCase;


    @DisplayName("기관은 선호물품을 등록할 수 있다. (controller)")
    @Test
    @MockUser(role = "ROLE_CENTER")
    void registerPreferItem() throws Exception {
        // given
        UUID centerId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        PreferItemCreateRequestDto requestDto = new PreferItemCreateRequestDto("어린이 도서");
        PreferItemCreateResponseDto responseDto = new PreferItemCreateResponseDto(
                1L,
                centerId,
                "어린이 도서"
        );

        given(createPreferItemUseCase.createPreferItem(centerId, requestDto))
                .willReturn(responseDto);

        // when & then
        mockMvc.perform(post("/api/preferItem")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("Authorization", "Bearer access-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("관심 기관 등록 성공"))
                .andExpect(jsonPath("$.data.id").value(responseDto.id()))
                .andExpect(jsonPath("$.data.center_id").value(responseDto.centerId().toString()))
                .andExpect(jsonPath("$.data.item_name").value(responseDto.itemName()));

        // verify
        verify(createPreferItemUseCase).createPreferItem(centerId, requestDto);
    }

    @DisplayName("존재하지 않는 기관 ID로 선호물품을 등록할 수 없다. (controller)")
    @Test
    @MockUser(role = "ROLE_CENTER")
    void registerPreferItem_Fail_WhenCenterNotExists() throws Exception {
        // given
        UUID centerId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        PreferItemCreateRequestDto requestDto = new PreferItemCreateRequestDto("어린이 도서");

        given(createPreferItemUseCase.createPreferItem(centerId, requestDto))
                .willThrow(new BadRequestException("존재하지 않는 기관입니다."));

        // when & then
        mockMvc.perform(post("/api/preferItem")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("Authorization", "Bearer access-token"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("존재하지 않는 기관입니다."));

        // verify
        verify(createPreferItemUseCase).createPreferItem(centerId, requestDto);
    }

    @DisplayName("기관은 등록한 선호물품을 삭제할 수 있다. (controller)")
    @Test
    @MockUser(role = "ROLE_CENTER")
    void deletePreferItem() throws Exception {
        // given
        UUID centerId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        Long preferItemId = 1L;

        // when & then
        mockMvc.perform(delete("/api/preferItem/{preferItemId}", preferItemId)
                        .principal(centerId::toString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("선호 물품 삭제 성공"));

        // verify
        verify(deletePreferItemUseCase).deletePreferItem(centerId, preferItemId);
    }

    @DisplayName("선호물품을 등록한 기관이 아니라면 선호물품을 삭제할 수 없다. (controller)")
    @Test
    @MockUser(role = "ROLE_CENTER")
    void deletePreferItem_Fail_WhenUnauthorized() throws Exception {
        // given
        UUID centerId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        Long preferItemId = 1L;

        doThrow(new BadRequestException("다른 기관의 선호 물품은 삭제할 수 없습니다."))
                .when(deletePreferItemUseCase)
                .deletePreferItem(centerId, preferItemId);

        // when & then
        mockMvc.perform(delete("/api/preferItem/{preferItemId}", preferItemId)
                        .principal(centerId::toString))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("다른 기관의 선호 물품은 삭제할 수 없습니다."));

        // verify
        verify(deletePreferItemUseCase).deletePreferItem(centerId, preferItemId);
    }

    @DisplayName("존재하지 않는 선호물품을 삭제할 수 없다. (controller)")
    @Test
    @MockUser(role = "ROLE_CENTER")
    void deletePreferItem_Fail_WhenPreferItemNotExists() throws Exception {
        // given
        UUID centerId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        Long preferItemId = 1L;

        doThrow(new BadRequestException("존재하지 않는 선호 물품입니다."))
                .when(deletePreferItemUseCase)
                .deletePreferItem(centerId, preferItemId);

        // when & then
        mockMvc.perform(delete("/api/preferItem/{preferItemId}", preferItemId)
                        .principal(centerId::toString))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("존재하지 않는 선호 물품입니다."));

        // verify
        verify(deletePreferItemUseCase).deletePreferItem(centerId, preferItemId);
    }
}
