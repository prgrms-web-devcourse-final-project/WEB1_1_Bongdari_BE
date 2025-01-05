package com.somemore.global.aspect.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.somemore.global.aspect.log.utils.SensitiveDataMasker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SensitiveDataMaskerTest {

    private SensitiveDataMasker sensitiveDataMasker;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        sensitiveDataMasker = new SensitiveDataMasker();
    }

    @DisplayName("민감한 필드 이름이 주어지면 데이터를 마스킹할 때 마스킹된 값을 반환해야 한다.")
    @Test
    void givenSensitiveField_whenMaskSensitiveData_thenMasked() throws JsonProcessingException {
        // Given
        String fieldName = "password";
        String value = "mySecretPassword";

        // When
        Object result = sensitiveDataMasker.maskSensitiveData(fieldName, value, objectMapper);

        // Then
        assertEquals("********", result);
    }

    @DisplayName("민감한 데이터가 포함된 Map에서 민감한 필드는 마스킹되어야 한다.")
    @Test
    void givenMapWithSensitiveData_whenMaskSensitiveData_thenMasked() throws JsonProcessingException {
        // Given
        Map<String, Object> data = new HashMap<>();
        data.put("password", "123456");
        data.put("username", "user123");

        // When
        Object result = sensitiveDataMasker.maskSensitiveData("testField", data, objectMapper);

        // Then
        assertEquals("********", ((Map<?, ?>) result).get("password"));
        assertEquals("user123", ((Map<?, ?>) result).get("username"));
    }

    @DisplayName("민감한 데이터를 포함하는 객체는 민감한 필드를 마스킹 해야한다.")
    @Test
    void givenComplexObject_whenMaskSensitiveData_thenMasked() throws JsonProcessingException {
        // Given
        Map<String, Object> nestedData = new HashMap<>();
        nestedData.put("token", "abcd1234");
        nestedData.put("email", "test@example.com");

        Map<String, Object> data = new HashMap<>();
        data.put("user", nestedData);
        data.put("username", "user123");

        // When
        Object result = sensitiveDataMasker.maskSensitiveData("testField", data, objectMapper);

        // Then
        Map<?, ?> maskedUser = (Map<?, ?>) ((Map<?, ?>) result).get("user");
        assertEquals("********", maskedUser.get("token"));
        assertEquals("test@example.com", maskedUser.get("email"));
        assertEquals("user123", ((Map<?, ?>) result).get("username"));
    }

    @DisplayName("일반 데이터는 마스킹되지 않아야 한다.")
    @Test
    void givenPrimitiveValue_whenMaskSensitiveData_thenUnchanged() throws JsonProcessingException {
        // Given
        int intValue = 12345;
        boolean boolValue = true;
        String textValue = "text";

        // When & Then
        assertEquals(intValue, sensitiveDataMasker.maskSensitiveData("field", intValue, objectMapper));
        assertEquals(boolValue, sensitiveDataMasker.maskSensitiveData("field", boolValue, objectMapper));
        assertEquals(textValue, sensitiveDataMasker.maskSensitiveData("field", textValue, objectMapper));
    }
}
