package com.somemore.imageupload.service;

import com.somemore.IntegrationTestSupport;
import com.somemore.global.exception.ImageUploadException;
import com.somemore.imageupload.dto.ImageUploadRequestDto;
import com.somemore.imageupload.validator.ImageUploadValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class ImageUploadServiceTest extends IntegrationTestSupport {

    @Mock
    private S3Client s3Client;

    @Mock
    private ImageUploadValidator imageUploadValidator;

    @InjectMocks
    private ImageUploadService imageUploadService;

    @Mock
    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() throws IOException {
        ReflectionTestUtils.setField(imageUploadService, "bucket", "test-bucket");
        ReflectionTestUtils.setField(imageUploadService, "baseUrl", "https://amazonaws.com/");

        when(multipartFile.getOriginalFilename()).thenReturn("testImage.jpg");
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(multipartFile.getInputStream()).thenReturn(mock(InputStream.class));
        when(multipartFile.getSize()).thenReturn(1000L);
    }

    @DisplayName("업로드 요청 이미지를 S3에 업로드 할 수 있다.")
    @Test
    void testUploadImage_success() {
        // given
        ImageUploadRequestDto requestDto = new ImageUploadRequestDto(multipartFile);

        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(null);

        // when
        String result = imageUploadService.uploadImage(requestDto);

        // then
        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        assertNotNull(result);
        assertTrue(result.startsWith("https://amazonaws.com/"));
        assertTrue(result.endsWith(".jpg"));
    }

    @DisplayName("이미지 형식이 올바르지 않다면 업로드 할 수 없다.")
    @Test
    void testUploadImage_failure() throws IOException {
        // given
        when(multipartFile.getInputStream()).thenThrow(new IOException());

        ImageUploadRequestDto requestDto = new ImageUploadRequestDto(multipartFile);

        // when, then
        assertThrows(ImageUploadException.class, () -> imageUploadService.uploadImage(requestDto));
    }

    @DisplayName("이미지 파일이 없다면 기본 이미지 링크를 반환한다.")
    @Test
    void uploadImageWithEmptyFile() {
        // given
        MultipartFile emptyFile = new MockMultipartFile("file", new byte[0]);
        given(imageUploadValidator.isEmptyFile(emptyFile)).willReturn(true);
        ImageUploadRequestDto requestDto = new ImageUploadRequestDto(emptyFile);

        // when
        String imgUrl = imageUploadService.uploadImage(requestDto);

        // then
        assertThat(imgUrl).isEqualTo(ImageUploadService.DEFAULT_IMAGE_URL);
    }
}
