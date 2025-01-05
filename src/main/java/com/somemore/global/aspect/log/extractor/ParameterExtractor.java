package com.somemore.global.aspect.log.extractor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.somemore.global.aspect.log.utils.SensitiveDataMasker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Component
public class ParameterExtractor {

    private final ObjectMapper objectMapper;
    private final SensitiveDataMasker sensitiveDataMasker;

    public String extractParameters(ProceedingJoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Parameter[] parameters = signature.getMethod().getParameters();
            Object[] args = joinPoint.getArgs();

            if (parameters.length == 0) {
                return "{}";
            }

            Map<String, Object> paramMap = new LinkedHashMap<>();
            for (int i = 0; i < parameters.length; i++) {
                if (args[i] != null) {
                    addParameter(paramMap, parameters[i], args[i]);
                }
            }

            return objectMapper.writeValueAsString(paramMap);
        } catch (Exception e) {
            log.warn("파라미터 변환 실패: {}", e.getMessage());
            return "{}";
        }
    }

    private void addParameter(Map<String, Object> paramMap, Parameter parameter, Object value) throws JsonProcessingException {
        String paramName = extractParamName(parameter);
        paramMap.put(paramName, sensitiveDataMasker.maskSensitiveData(paramName, value, objectMapper));
    }

    private String extractParamName(Parameter parameter) {
        PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
        if (pathVariable != null && !pathVariable.value().isEmpty()) {
            return pathVariable.value();
        }

        RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
        if (requestParam != null && !requestParam.value().isEmpty()) {
            return requestParam.value();
        }

        return parameter.getName();
    }
}
