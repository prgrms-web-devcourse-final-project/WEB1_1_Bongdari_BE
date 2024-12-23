package com.somemore.domains.volunteer.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.somemore.domains.volunteer.dto.request.VolunteerProfileUpdateRequestDto;
import com.somemore.domains.volunteer.usecase.UpdateVolunteerProfileUseCase;
import com.somemore.global.imageupload.usecase.ImageUploadUseCase;
import com.somemore.support.ControllerTestSupport;
import com.somemore.support.annotation.WithMockCustomUser;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

class VolunteerProfileCommandControllerTest extends ControllerTestSupport {

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
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("유효성 예외"))
                .andExpect(jsonPath("$.detail").value("닉네임은 최대 10자까지 입력 가능합니다."));
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
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("유효성 예외"))
                .andExpect(jsonPath("$.detail").value("소개글은 최대 100자까지 입력 가능합니다."));
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
