package com.allforone.starvestop.common.dto;

import com.allforone.starvestop.common.enums.SuccessMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "공통 응답 포맷")
public class CommonResponse <T> {

    @Schema(example = "true", description = "성공 여부")
    private boolean success;
    @Schema(example = "로그인 성공", description = "응답 메시지")
    private String message;
    @Schema(description = "응답 데이터")
    private T data;
    @Schema(example = "2026-02-13T20:10:30.123", description = "응답 시각")
    private LocalDateTime timestamp;

    // 성공 시 (사용 예시: CommonResponse.success(SuccessMessage 이넘, 반환 DTO)
    public static <T> CommonResponse<T> success(SuccessMessage successMessage, T data) {
        return new CommonResponse<>(true, successMessage.getMessage(), data, LocalDateTime.now());
    }

    // 성공 했는데 응답 데이터는 없을 시 (사용 예시: CommonResponse.successNodata(SuccessMessage 이넘)
    public static CommonResponse<Void> successNoData(SuccessMessage successMessage) {
        return new CommonResponse<>(true, successMessage.getMessage(), null, LocalDateTime.now());
    }

    // 예외 처리 시
    public static CommonResponse<Void> exception(String errorMessage) {
        return new CommonResponse<>(false, errorMessage, null, LocalDateTime.now());
    }
}
