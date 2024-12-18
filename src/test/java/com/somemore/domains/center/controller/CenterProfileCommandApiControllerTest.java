package com.somemore.domains.center.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.somemore.domains.center.dto.request.CenterProfileUpdateRequestDto;
import com.somemore.domains.center.usecase.command.UpdateCenterProfileUseCase;
import com.somemore.global.imageupload.usecase.ImageUploadUseCase;
import com.somemore.support.ControllerTestSupport;
import com.somemore.support.annotation.WithMockCustomUser;
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

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CenterProfileCommandApiControllerTest extends ControllerTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UpdateCenterProfileUseCase updateCenterProfileUseCase;

    @MockBean
    private ImageUploadUseCase imageUploadUseCase;

    @Test
    @DisplayName("센터 프로필 수정 성공 테스트")
    @WithMockCustomUser(role = "CENTER")
    void updateCenterProfile_success() throws Exception {
        //given
        CenterProfileUpdateRequestDto requestDto = CenterProfileUpdateRequestDto.builder()
                .name("테스트 센터명")
                .contactNumber("010-0000-0000")
                .homepageLink("https://www.test.com")
                .introduce("테스트 설명")
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

        String imageUrl = "http://example.com/image/test-image.jpg";

        given(imageUploadUseCase.uploadImage(any())).willReturn(imageUrl);
        willDoNothing().given(updateCenterProfileUseCase)
                .updateCenterProfile(any(UUID.class), any(), anyString());

        MockMultipartHttpServletRequestBuilder builder = multipart("/api/center/profile");
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        //when
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
                .andExpect(jsonPath("$.message").value("센터 프로필 수정 성공"));
    }
}
