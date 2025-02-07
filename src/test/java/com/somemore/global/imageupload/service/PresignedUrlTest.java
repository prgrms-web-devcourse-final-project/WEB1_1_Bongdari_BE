package com.somemore.global.imageupload.service;

import com.somemore.global.exception.ImageUploadException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.somemore.global.exception.ExceptionMessage.UPLOAD_FAILED;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PresignedUrlTest {

    @Test
    @DisplayName("쿼리 문자열이 포함된 URL에서 removeQueryString() 호출 시 쿼리 문자열 제거 후 반환")
    void removeQueryString_shouldReturnUrlWithoutQuery() {
        // given
        String url = "https://example.com/file.png?param=value";
        PresignedUrl presignedUrl = PresignedUrl.from(url);

        // when
        String result = presignedUrl.removeQueryString();

        // then
        assertEquals("https://example.com/file.png", result);
    }

    @Test
    @DisplayName("쿼리 문자열이 없는 URL을 PresignedUrl.from()으로 생성하면 예외 발생")
    void from_shouldThrowException_whenUrlHasNoQueryString() {
        // given
        String url = "https://example.com/file.png";

        // when
        // then
        ImageUploadException exception = assertThrows(ImageUploadException.class,
                () -> PresignedUrl.from(url));

        assertEquals(UPLOAD_FAILED.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("null 값을 PresignedUrl.from()으로 전달하면 예외 발생")
    void from_shouldThrowException_whenUrlIsNull() {
        // given
        String url = null;

        // when
        // then
        assertThrows(ImageUploadException.class,
                () -> PresignedUrl.from(url));
    }

    @Test
    @DisplayName("removeQueryString() 호출 시 올바르게 동작하고 예외가 발생하지 않음")
    void removeQueryString_shouldWorkCorrectly() {
        // given
        String url = "https://example.com/file.png?param=value";
        PresignedUrl presignedUrl = PresignedUrl.from(url);

        // when
        // then
        assertDoesNotThrow(presignedUrl::removeQueryString);
    }
}