package com.somemore.global.imageupload.service;

import com.somemore.global.exception.ImageUploadException;
import lombok.extern.slf4j.Slf4j;

import static com.somemore.global.exception.ExceptionMessage.UPLOAD_FAILED;

@Slf4j
public record PresignedUrl(
        String value
) {
    public String removeQueryString() {
        int queryIndex = value.indexOf('?');
        if (queryIndex == -1) {
            log.error("PresignedUrl({})에 쿼리스트링이 없어서 업로드 할 수 없습니다", value);
            throw new ImageUploadException(UPLOAD_FAILED);
        }
        return value.substring(0, queryIndex);
    }

    public static PresignedUrl from(String url) {
        if (url.contains("?")) {
            return new PresignedUrl(url);
        }
        log.error("PresignedUrl({})이 올바르지 않은 형식입니다.", url);
        throw new ImageUploadException(UPLOAD_FAILED);
    }
}
