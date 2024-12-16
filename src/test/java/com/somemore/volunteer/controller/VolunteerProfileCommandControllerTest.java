package com.somemore.volunteer.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.somemore.ControllerTestSupport;
import com.somemore.WithMockCustomUser;
import com.somemore.global.imageupload.usecase.ImageUploadUseCase;
import com.somemore.volunteer.dto.request.VolunteerProfileUpdateRequestDto;
import com.somemore.volunteer.usecase.UpdateVolunteerProfileUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VolunteerProfileCommandControllerTest extends ControllerTestSupport {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UpdateVolunteerProfileUseCase updateVolunteerProfileUseCase;

    @MockBean
    private ImageUploadUseCase imageUploadUseCase;

    @DisplayName("봉사자 프로필 수정 성공 테스트")
    @Test
    @WithMockCustomUser(role = "VOLUNTEER")
    void updateVolunteerProfile() throws Exception {
        // given
        VolunteerProfileUpdateRequestDto requestDto = VolunteerProfileUpdateRequestDto.builder()
                .nickname("making")
                .introduce("making is making")
                .build();

        MockMultipartFile imageFile = createMockImageFile();
        MockMultipartFile requestData = createMockRequestData(requestDto);

        String mockImageUrl = "http://example.com/image/profile-image.jpg";

        given(imageUploadUseCase.uploadImage(any())).willReturn(mockImageUrl);
        willDoNothing().given(updateVolunteerProfileUseCase)
                .update(any(UUID.class), any(VolunteerProfileUpdateRequestDto.class), anyString());

        MockMultipartHttpServletRequestBuilder builder = createMockRequestBuilder();

        // when
        mockMvc.perform(builder
                        .file(requestData)
                        .file(imageFile)
                        .contentType(MULTIPART_FORM_DATA)
                        .header("Authorization", "Bearer access-token"))
                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("프로필 수정 성공"));
    }

    @DisplayName("봉사자 프로필 수정의 닉네임 유효성 검사 실패 테스트")
    @Test
    @WithMockCustomUser(role = "VOLUNTEER")
    void testNicknameValidation() throws Exception {
        // given
        VolunteerProfileUpdateRequestDto invalidRequestDto = VolunteerProfileUpdateRequestDto.builder()
                .nickname("TooLongNickname") // 10자를 초과
                .introduce("Valid introduction")
                .build();

        MockMultipartFile imageFile = createMockImageFile();
        MockMultipartFile requestData = createMockRequestData(invalidRequestDto);

        MockMultipartHttpServletRequestBuilder builder = createMockRequestBuilder();

        // when
        mockMvc.perform(builder
                        .file(requestData)
                        .file(imageFile)
                        .contentType(MULTIPART_FORM_DATA)
                        .header("Authorization", "Bearer access-token"))
                .andExpect(status().isBadRequest())
                .andDo(result -> {
                    String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

                    Map<String, Object> jsonResponse = objectMapper.readValue(responseBody, new TypeReference<>() {
                    });
                    String detail = (String) jsonResponse.get("detail");

                    assertThat(detail).isEqualTo("입력 데이터 유효성 검사가 실패했습니다. 각 필드를 확인해주세요.");
                });
    }

    @DisplayName("봉사자 프로필 수정의 소개 유효성 검사 실패 테스트")
    @Test
    @WithMockCustomUser(role = "VOLUNTEER")
    void testIntroduceValidation() throws Exception {
        // given
        VolunteerProfileUpdateRequestDto invalidRequestDto = VolunteerProfileUpdateRequestDto.builder()
                .nickname("making")
                .introduce("""
                        TOOLONG_TOOLONG_TOOLONG_TOOLONG_TOOLONG_TOOLONG_TOOLONG_TOOLONG_
                        TOOLONG_TOOLONG_TOOLONG_TOOLONG_TOOLONG_TOOLONG_TOOLONG_TOOLONG_
                        TOOLONG_TOOLONG_TOOLONG_TOOLONG_TOOLONG_TOOLONG_TOOLONG_TOOLONG_
                        TOOLONG_TOOLONG_TOOLONG_TOOLONG_TOOLONG_TOOLONG_TOOLONG_TOOLONG_
                        TOOLONG_TOOLONG_TOOLONG_TOOLONG_TOOLONG_TOOLONG_TOOLONG_TOOLONG_
                        TOOLONG_TOOLONG_TOOLONG_TOOLONG_TOOLONG_TOOLONG_TOOLONG_TOOLONG_
                        """)
                .build();

        MockMultipartFile imageFile = createMockImageFile();
        MockMultipartFile requestData = createMockRequestData(invalidRequestDto);

        MockMultipartHttpServletRequestBuilder builder = createMockRequestBuilder();

        // when
        mockMvc.perform(builder
                        .file(requestData)
                        .file(imageFile)
                        .contentType(MULTIPART_FORM_DATA)
                        .header("Authorization", "Bearer access-token"))
                .andExpect(status().isBadRequest())
                .andDo(result -> {
                    String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

                    Map<String, Object> jsonResponse = objectMapper.readValue(responseBody, new TypeReference<>() {
                    });
                    String detail = (String) jsonResponse.get("detail");

                    assertThat(detail).isEqualTo("입력 데이터 유효성 검사가 실패했습니다. 각 필드를 확인해주세요.");
                });
    }

    private MockMultipartFile createMockImageFile() {
        return new MockMultipartFile(
                "img_file",
                "profile-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "profile image content".getBytes()
        );
    }

    private MockMultipartFile createMockRequestData(Object dto) throws Exception {
        return new MockMultipartFile(
                "data",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(dto)
        );
    }

    private MockMultipartHttpServletRequestBuilder createMockRequestBuilder() {
        MockMultipartHttpServletRequestBuilder builder = multipart("/api/profile");
        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });
        return builder;
    }
}
