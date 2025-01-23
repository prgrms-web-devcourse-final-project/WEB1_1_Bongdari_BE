package com.somemore.global.imageupload.util;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.INSTANTIATION_NOT_ALLOWED;

public class ImageUploadUtils {

    private ImageUploadUtils() {
        throw new UnsupportedOperationException(INSTANTIATION_NOT_ALLOWED.getMessage());
    }

    public static String generateUniqueFileName(String originalFileName) {

        String uuid = UUID.randomUUID().toString();

        String fileExtension = extractFileExtension(originalFileName);

        String fileNameWithoutExtension = originalFileName.substring(0, originalFileName.lastIndexOf("."));

        return uuid + "_" + fileNameWithoutExtension + fileExtension;
    }

    private static String extractFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public static String generateS3Url(String baseUrl, String fileName) {
        return String.format("%s/%s", baseUrl, fileName);
    }

}
