package com.somemore.global.imageupload.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.somemore.global.exception.ImageUploadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class DefaultImageUploadValidatorTest {

    private DefaultImageUploadValidator imageUploadValidator;

    @BeforeEach
    void setUp() {
        // given
        imageUploadValidator = new DefaultImageUploadValidator();
    }

    @Test
    @DisplayName("파일이 비어있는지 확인할 수 있다.")
    void shouldThrowExceptionWhenFileIsEmpty() {
        //given
        MultipartFile emptyFile = new MockMultipartFile("file", new byte[0]);

        //when
        boolean isEmpty = imageUploadValidator.isEmptyFile(emptyFile);

        //then
        assertThat(isEmpty).isTrue();
    }

    @Test
    @DisplayName("파일 크기가 최대 8MB를 초과하는 경우, 예외가 발생한다.")
    void shouldThrowExceptionWhenFileSizeExceeded() {
        // given
        MultipartFile largeFile = new MockMultipartFile("file", "largeImage.jpg", "image/jpeg",
                new byte[9 * 1024 * 1024]);

        // when
        Throwable exception = assertThrows(ImageUploadException.class,
                () -> imageUploadValidator.validateFileSize(largeFile));

        // then
        assertEquals(ImageUploadException.class, exception.getClass());
    }

    @Test
    @DisplayName("유효한 이미지 타입(JPEG) 파일은, 검증에 통과한다.")
    void shouldNotThrowExceptionWhenFileTypeIsValidJpeg() {
        // given
        MultipartFile validFile = new MockMultipartFile("file", "validImage.jpg", "image/jpeg",
                new byte[1024]);

        // when
        imageUploadValidator.validateFileType(validFile);

        // then
        assertDoesNotThrow(() -> imageUploadValidator.validateFileType(validFile));
    }

    @Test
    @DisplayName("유효하지 않은 이미지 타입 파일이 있을 경우, 예외가 발생한다.")
    void shouldThrowExceptionWhenFileTypeIsInvalid() {
        // given
        MultipartFile invalidFile = new MockMultipartFile("file", "invalidFile.pdf",
                "application/pdf", new byte[1024]);

        // when
        Throwable exception = assertThrows(ImageUploadException.class,
                () -> imageUploadValidator.validateFileType(invalidFile));

        // then
        assertEquals(ImageUploadException.class, exception.getClass());
    }

    @Test
    @DisplayName("파일 타입이 올바르지 않을 경우, 예외가 발생한다.")
    void shouldThrowExceptionWhenFileTypeIsNull() {
        // given
        MultipartFile nullContentTypeFile = new MockMultipartFile("file", "noContentTypeFile.jpg",
                null, new byte[1024]);

        // when
        Throwable exception = assertThrows(ImageUploadException.class,
                () -> imageUploadValidator.validateFileType(nullContentTypeFile));

        // then
        assertEquals(ImageUploadException.class, exception.getClass());
    }

    @Test
    @DisplayName("파일 이름이 비어있는지 확인할 수 있다.")
    void shouldReturnTrueWhenFileNameIsEmpty() {
        // given
        String emptyFileName = "";

        // when
        boolean isEmptyFileName = imageUploadValidator.isEmptyFileName(emptyFileName);

        // then
        assertThat(isEmptyFileName).isTrue();
    }

    @Test
    @DisplayName("파일 이름이 null이면 true를 반환한다.")
    void shouldReturnTrueWhenFileNameIsNull() {
        // given
        String nullFileName = null;

        // when
        boolean isEmptyFileName = imageUploadValidator.isEmptyFileName(nullFileName);

        // then
        assertThat(isEmptyFileName).isTrue();
    }

    @Test
    @DisplayName("파일 이름이 비어있지 않으면 false를 반환한다.")
    void shouldReturnFalseWhenFileNameIsNotEmpty() {
        // given
        String validFileName = "testImage.jpg";

        // when
        boolean isEmptyFileName = imageUploadValidator.isEmptyFileName(validFileName);

        // then
        assertThat(isEmptyFileName).isFalse();
    }
}
