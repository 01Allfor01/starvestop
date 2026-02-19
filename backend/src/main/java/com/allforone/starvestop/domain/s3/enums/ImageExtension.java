package com.allforone.starvestop.domain.s3.enums;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ImageExtension {
    PNG("png", "image/png"),
    JPG("jpg", "image/jpeg"),
    JPEG("jpeg", "image/jpeg"),

    ;

    private final String extension;
    private final String contentType;

    ImageExtension(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public static String from(String extension) {
        ImageExtension imageExtension = Arrays.stream(values())
                .filter(type -> type.extension.equalsIgnoreCase(extension))
                .findFirst()
                .orElseThrow( () -> new CustomException(ErrorCode.IMAGE_EXTENSION_BAD_REQUEST));
        return imageExtension.contentType;
    }
}
