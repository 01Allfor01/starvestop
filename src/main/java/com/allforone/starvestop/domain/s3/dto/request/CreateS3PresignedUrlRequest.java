package com.allforone.starvestop.domain.s3.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateS3PresignedUrlRequest {
    private Long id;
    private String name;
}