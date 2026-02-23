package com.allforone.starvestop.domain.s3.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateS3PresignedUrlResponse {
    private final String imageUuid;
    private final String uploadUrl;
}