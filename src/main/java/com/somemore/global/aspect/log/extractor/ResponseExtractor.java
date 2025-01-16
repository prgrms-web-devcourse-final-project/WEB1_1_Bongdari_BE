package com.somemore.global.aspect.log.extractor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.somemore.global.common.response.LoggedResponse;
import com.somemore.global.exception.BadRequestException;
import com.somemore.global.exception.DuplicateException;
import com.somemore.global.exception.ImageUploadException;
import com.somemore.global.exception.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RequiredArgsConstructor
@Slf4j
@Component
public class ResponseExtractor {

    private final ObjectMapper objectMapper;

    public LoggedResponse extractResponse(Object result) {
        try {
            if (result == null) {
                return new LoggedResponse(HttpStatus.OK, "null");
            }

            if (result instanceof ResponseEntity<?> responseEntity) {
                String body = objectMapper.writeValueAsString(responseEntity.getBody());
                return new LoggedResponse(responseEntity.getStatusCode(), body);
            }

            return new LoggedResponse(HttpStatus.OK, objectMapper.writeValueAsString(result));
        } catch (Exception e) {
            log.warn("응답 변환 실패: {}", e.getMessage());
            return new LoggedResponse(HttpStatus.OK, "[응답 변환 실패]");
        }
    }

    public HttpStatus extractExceptionStatus(Exception e) {
        if (e instanceof BadRequestException ||
                e instanceof ImageUploadException ||
                e instanceof DuplicateException ||
                e instanceof MethodArgumentNotValidException) {
            return HttpStatus.BAD_REQUEST;
        } else if (e instanceof NoSuchElementException) {
            return HttpStatus.NOT_FOUND;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
