package com.somemore.global.aspect.log.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class SensitiveDataMasker {

    private final Set<String> sensitiveFields = new HashSet<>(Arrays.asList(
            "password", "token", "secret", "credential", "authorization",
            "accessToken", "refreshToken"
    ));

    public Object maskSensitiveData(String fieldName, Object value, ObjectMapper objectMapper) throws JsonProcessingException {
        if (isSensitiveField(fieldName)) {
            return "********";
        }
        if (value instanceof Map) {
            return maskSensitiveDataInMap((Map<?, ?>) value);
        }
        if (isComplexObject(value)) {
            String json = objectMapper.writeValueAsString(value);
            json = maskSensitiveDataInJson(json, objectMapper);
            return objectMapper.readValue(json, Object.class);
        }
        return value;
    }

    private boolean isSensitiveField(String fieldName) {
        String lowercaseFieldName = fieldName.toLowerCase();
        return sensitiveFields.stream()
                .anyMatch(sensitive -> lowercaseFieldName.contains(sensitive.toLowerCase()));
    }

    private Map<String, Object> maskSensitiveDataInMap(Map<?, ?> map) {
        Map<String, Object> maskedMap = new LinkedHashMap<>();

        map.forEach((key, value) -> {
            String keyStr = String.valueOf(key);
            if (isSensitiveField(keyStr)) {
                maskedMap.put(keyStr, "********");
            } else if (value instanceof Map) {
                maskedMap.put(keyStr, maskSensitiveDataInMap((Map<?, ?>) value));
            } else {
                maskedMap.put(keyStr, value);
            }
        });

        return maskedMap;
    }

    private String maskSensitiveDataInJson(String json, ObjectMapper objectMapper) {
        try {
            Map<String, Object> jsonMap = objectMapper.readValue(json, new TypeReference<>() {});
            Map<String, Object> maskedMap = maskSensitiveDataInMap(jsonMap);
            return objectMapper.writeValueAsString(maskedMap);
        } catch (Exception e) {
            log.warn("JSON 마스킹 처리 실패: {}", e.getMessage());
            return json;
        }
    }

    private boolean isComplexObject(Object value) {
        return !(value instanceof String || value instanceof Number ||
                value instanceof Boolean || value instanceof Date);
    }
}
