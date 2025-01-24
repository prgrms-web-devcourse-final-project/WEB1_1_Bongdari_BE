package com.somemore.center.controller;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_CENTER;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.somemore.center.dto.response.CenterProfileResponseDto;
import com.somemore.center.usecase.NEWCenterQueryUseCase;
import com.somemore.global.exception.BadRequestException;
import com.somemore.support.ControllerTestSupport;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

class CenterQueryApiControllerTest extends ControllerTestSupport {

    @MockBean
    protected NEWCenterQueryUseCase centerQueryUseCase;

    private UUID centerId;
    private CenterProfileResponseDto responseDto;

    @BeforeEach
    void setUp() {
        centerId = UUID.randomUUID();
        responseDto = CenterProfileResponseDto.builder()
                .id(centerId)
                .userId(UUID.randomUUID())
                .homepageUrl("http://example.com")
                .name("Test Center")
                .contactNumber("010-1234-5678")
                .imgUrl("http://example.com/image.jpg")
                .introduce("This is a test center.")
                .preferItems(List.of())
                .build();
    }

    @DisplayName("기관 ID로 기관 프로필을 조회할 수 있다. (controller)")
    @Test
    void getCenterProfile() throws Exception {
        // given
        when(centerQueryUseCase.getCenterProfileById(centerId)).thenReturn(responseDto);

        // when & then
        mockMvc.perform(
                        get("/api/center/profile/{centerId}", centerId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("기관 프로필 조회 성공"))
                .andExpect(jsonPath("$.data.id").value(centerId.toString()))
                .andExpect(jsonPath("$.data.user_id").value(responseDto.userId().toString()))
                .andExpect(jsonPath("$.data.homepage_url").value(responseDto.homepageUrl()))
                .andExpect(jsonPath("$.data.name").value(responseDto.name()))
                .andExpect(jsonPath("$.data.contact_number").value(responseDto.contactNumber()))
                .andExpect(jsonPath("$.data.img_url").value(responseDto.imgUrl()))
                .andExpect(jsonPath("$.data.introduce").value(responseDto.introduce()))
                .andExpect(jsonPath("$.data.prefer_items").isArray());

        verify(centerQueryUseCase, times(1)).getCenterProfileById(centerId);
    }

    @DisplayName("존재하지 않는 유저 ID로 조회 시 예외가 발생한다. (controller)")
    @Test
    void getCenterProfile_NotFound() throws Exception {
        // given
        UUID nonExistentuserId = UUID.randomUUID();
        when(centerQueryUseCase.getCenterProfileById(nonExistentuserId))
                .thenThrow(new BadRequestException(NOT_EXISTS_CENTER.getMessage()));

        // when & then
        mockMvc.perform(
                        get("/api/center/profile/{centerId}", nonExistentuserId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.detail").value("존재하지 않는 기관입니다."));

        verify(centerQueryUseCase, times(1)).getCenterProfileById(nonExistentuserId);
    }

}
