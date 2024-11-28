package com.somemore.imageupload.validator;

import com.somemore.global.exception.ImageUploadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

class DefaultImageUploadValidatorTest {

    private DefaultImageUploadValidator imageUploadValidator;

    @BeforeEach
    void setUp() {
        imageUploadValidator = new DefaultImageUploadValidator();
    }

    @Test
    @DisplayName("파일이 비어있으면 예외가 발생한다.")
    void shouldThrowExceptionWhenFileIsEmpty() {
        //given
        MultipartFile emptyFile = new MockMultipartFile("file", new byte[0]);

        //when&then
        assertThrows(ImageUploadException.class, () -> imageUploadValidator.validateFileSize(emptyFile));
    }

    @Test
    @DisplayName("파일 크기가 최대 8MB를 초과하는 경우, 예외가 발생한다.")
    void shouldThrowExceptionWhenFileSizeExceeded() {
        //given
        MultipartFile largeFile = new MockMultipartFile("file", "largeImage.jpg", "image/jpeg", new byte[9 * 1024 * 1024]);

        //when&then
        assertThrows(ImageUploadException.class, () -> imageUploadValidator.validateFileSize(largeFile));
    }

    @Test
    @DisplayName("유효한 이미지 타입(JPEG) 파일이 있을 경우, 검증에 통과한다.")
    void shouldNotThrowExceptionWhenFileTypeIsValidJpeg() {
        //given
        MultipartFile validFile = new MockMultipartFile("file", "validImage.jpg", "image/jpeg", new byte[1024]);

        //when&then
        assertDoesNotThrow(() -> imageUploadValidator.validateFileType(validFile));
    }

    @Test
    @DisplayName("유효하지 않은 이미지 타입 파일이 있을 경우, 예외가 발생한다.")
    void shouldThrowExceptionWhenFileTypeIsInvalid() {
        //given
        MultipartFile invalidFile = new MockMultipartFile("file", "invalidFile.pdf", "application/pdf", new byte[1024]);

        //when&then
        assertThrows(ImageUploadException.class, () -> imageUploadValidator.validateFileType(invalidFile));
    }

    @Test
    @DisplayName("파일 타입이 올바르지 않을 경우, 예외가 발생한다.")
    void shouldThrowExceptionWhenFileTypeIsNull() {
        //given
        MultipartFile nullContentTypeFile = new MockMultipartFile("file", "noContentTypeFile.jpg", null, new byte[1024]);

        //when&then
        assertThrows(ImageUploadException.class, () -> imageUploadValidator.validateFileType(nullContentTypeFile));
    }
}
