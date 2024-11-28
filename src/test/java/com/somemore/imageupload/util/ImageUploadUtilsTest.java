package com.somemore.imageupload.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageUploadUtilsTest {

    @DisplayName("이미지 업로드시 유일한 이미지 이름을 만들어줄 수 있다.")
    @Test
    void testGenerateUniqueFileName() {
        //given
        String fileName = "image.png";

        //when
        String uniqueName = ImageUploadUtils.generateUniqueFileName(fileName);

        //then
        assertTrue(uniqueName.endsWith(".png"));
        assertNotEquals(fileName, uniqueName);
    }

    @DisplayName("이미지의 확장자를 검증할 수 있다.")
    @Test
    void testExtractFileExtension() {
        //given
        String fileName = "example.jpg";

        //when
        String extension = ImageUploadUtils.extractFileExtension(fileName);

        //then
        assertEquals(".jpg", extension);
    }

    @DisplayName("이미지의 주소를 반환할 수 있다.")
    @Test
    void testGenerateS3Url() {
        //given
        String baseUrl = "https://amazonaws.com";
        String fileName = "unique-image.png";

        //when
        String url = ImageUploadUtils.generateS3Url(baseUrl, fileName);

        //then
        assertEquals("https://amazonaws.com/unique-image.png", url);
    }
}