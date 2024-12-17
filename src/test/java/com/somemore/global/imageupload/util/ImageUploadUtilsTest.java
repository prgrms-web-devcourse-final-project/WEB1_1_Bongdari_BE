package com.somemore.global.imageupload.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ImageUploadUtilsTest {

    @Test
    void privateConstructorShouldThrowException() throws Exception {
        // given
        Constructor<ImageUploadUtils> constructor = ImageUploadUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class,
                constructor::newInstance);

        // then
        assertThrows(UnsupportedOperationException.class, () -> {
            throw exception.getCause();
        });
    }

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

    @DisplayName("유니크한 파일 이름을 생성할 때 UUID는 정상적으로 생성된다.")
    @Test
    void testGenerateUniqueFileName_uuid() {
        // given
        String fileName = "image.png";

        // when
        String uniqueName = ImageUploadUtils.generateUniqueFileName(fileName);

        // then
        assertNotNull(uniqueName);
        assertTrue(uniqueName.contains("-"));
        assertTrue(uniqueName.endsWith(".png"));
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

    @DisplayName("baseUrl이 빈 문자열일 경우 URL을 생성할 수 있다.")
    @Test
    void testGenerateS3Url_emptyBaseUrl() {
        // given
        String baseUrl = "";
        String fileName = "unique-image.png";

        // when
        String url = ImageUploadUtils.generateS3Url(baseUrl, fileName);

        // then
        assertEquals("/unique-image.png", url);
    }

}
