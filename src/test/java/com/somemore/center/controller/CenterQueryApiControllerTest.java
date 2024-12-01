package com.somemore.center.controller;

import com.somemore.ControllerTestSupport;
import com.somemore.center.dto.response.CenterProfileResponseDto;
import com.somemore.center.usecase.query.CenterQueryUseCase;
import com.somemore.global.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_CENTER;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class CenterQueryApiControllerTest extends ControllerTestSupport {

    @MockBean
    protected CenterQueryUseCase centerQueryUseCase;

    private UUID centerId;
    private CenterProfileResponseDto responseDto;

    @BeforeEach
    void setUp() {
        centerId = UUID.randomUUID();
        responseDto = CenterProfileResponseDto.builder()
                .centerId(centerId)
                .name("Test Center")
                .contactNumber("010-1234-5678")
                .imgUrl("http://example.com/image.jpg")
                .introduce("This is a test center.")
                .homepageLink("http://example.com")
                .preferItems(List.of())
                .build();
    }

    @DisplayName("기관 ID로 기관 프로필을 조회할 수 있다. (controller)")
    @Test
    void getCenterProfile() throws Exception {
        // given
        when(centerQueryUseCase.getCenterProfileByCenterId(centerId)).thenReturn(responseDto);

        // when // then
        mockMvc.perform(
                        get("/api/center/profile/{centerId}", centerId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("기관 프로필 조회 성공"))
                .andExpect(jsonPath("$.data.center_id").value(centerId.toString())) // center_id로 수정
                .andExpect(jsonPath("$.data.name").value("Test Center"))
                .andExpect(jsonPath("$.data.contact_number").value("010-1234-5678")) // contact_number로 수정
                .andExpect(jsonPath("$.data.img_url").value("http://example.com/image.jpg")) // img_url로 수정
                .andExpect(jsonPath("$.data.introduce").value("This is a test center."))
                .andExpect(jsonPath("$.data.homepage_link").value("http://example.com")) // homepage_link로 수정
                .andExpect(jsonPath("$.data.prefer_items").isArray()); // prefer_items로 수정

        verify(centerQueryUseCase, times(1)).getCenterProfileByCenterId(centerId);
    }

    @DisplayName("존재하지 않는 기관 ID로 조회 시 예외가 발생한다. (controller)")
    @Test
    void getCenterProfile_NotFound() throws Exception {
        // given
        UUID nonExistentCenterId = UUID.randomUUID();
        when(centerQueryUseCase.getCenterProfileByCenterId(nonExistentCenterId))
                .thenThrow(new BadRequestException(NOT_EXISTS_CENTER.getMessage()));

        // when // then
        mockMvc.perform(
                        get("/api/center/profile/{centerId}", nonExistentCenterId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.detail").value("존재하지 않는 기관입니다."));

        verify(centerQueryUseCase, times(1)).getCenterProfileByCenterId(nonExistentCenterId);
    }

}

