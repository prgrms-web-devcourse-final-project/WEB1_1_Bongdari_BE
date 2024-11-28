package com.somemore.imageupload.util;

import java.util.UUID;

public final class ImageUploadUtils {

    private ImageUploadUtils() {
        throw new UnsupportedOperationException("인스턴스화 할 수 없는 클래스 입니다.");
    }

    public static String generateUniqueFileName(String originalFileName) {
        String uuid = UUID.randomUUID().toString();
        String fileExtension = extractFileExtension(originalFileName);
        return uuid + fileExtension;
    }

    public static String extractFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public static String generateS3Url(String baseUrl, String fileName) {
        return String.format("%s/%s", baseUrl, fileName);
    }

}
