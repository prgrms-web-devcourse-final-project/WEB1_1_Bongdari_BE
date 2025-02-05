package com.somemore.center.controller;

import com.somemore.center.dto.request.CenterProfileImgUpdateRequestDto;
import com.somemore.center.usecase.UpdateCenterProfileImgUseCase;
import com.somemore.support.ControllerTestSupport;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

class CenterCommandApiControllerTest extends ControllerTestSupport {

    @MockBean
    private UpdateCenterProfileImgUseCase updateCenterProfileImgUseCase;

    @DisplayName("기관 프로필 이미지를 수정할 수 있다. (controller)")
    @Test
    void updateCenterProfile() throws Exception {

        // given
        UUID centerId = UUID.randomUUID();
        CenterProfileImgUpdateRequestDto requestDto = new CenterProfileImgUpdateRequestDto("test.jpg");

        String expectedPresignedUrl = "https://example.com/presigned-url/test.jpg";
        when(updateCenterProfileImgUseCase.updateCenterProfileImg(centerId, requestDto))
                .thenReturn(expectedPresignedUrl);

        // when & then
        mockMvc.perform(
                        put("/api/center/profileImgUpdate")
                                .header("X-User-Id", centerId.toString()) // @UserId를 시뮬레이션
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("센터 프로필 수정 성공"))
                .andExpect(jsonPath("$.data").value(expectedPresignedUrl));

        verify(updateCenterProfileImgUseCase, times(1)).updateCenterProfileImg(centerId, requestDto);
    }

}