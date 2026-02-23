package com.allforone.starvestop.domain.subscription.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Schema(description = "구독 생성 요청")
public class CreateSubscriptionRequest {
    @Schema(description = "구독 이름", example = "평일 점심 구독")
    @NotBlank(message = "구독 이름을 적어주세요")
    @Size(max = 100, message = "구독 이름은 최대 100자까지 입력 가능합니다.")
    private String name;

    @Schema(description = "구독 설명", example = "월~금 점심 픽업 구독")
    @NotBlank(message = "구독 설명을 적어주세요")
    @Size(max = 255, message = "구독 설명은 최대 255자까지 입력 가능합니다.")
    private String description;

    @Schema(description = """
            요일 비트마스크 값.
            MONDAY=1, TUESDAY=2, WEDNESDAY=4, THURSDAY=8,
            FRIDAY=16, SATURDAY=32, SUNDAY=64
            
            예:
            - 월요일 = 1
            - 월~금 = 31
            - 토/일 = 96
            """, example = "31")
    @NotNull(message = "요일을 선택 해 주세요")
    @Min(value = 1, message = "요일을 잘못 입력했습니다")
    @Max(value = 127, message = "요일을 잘못 입력했습니다")
    private int day;

    @Schema(description = """
            식사 시간 비트마스크 값.
            BREAKFAST=1, LUNCH=2, DINNER=4
            
            예:
            - 점심 = 2
            - 점심+저녁 = 6
            """, example = "2")
    @NotNull(message = "식사 시간을 선택 해 주세요")
    @Min(value = 1, message = "식사 시간을 잘못 입력했습니다")
    @Max(value = 7, message = "식사 시간을 잘못 입력했습니다")
    private int mealTime;

    @Schema(description = "가격", example = "5900")
    @NotNull(message = "가격을 적어주세요")
    private BigDecimal price;

    @Schema(description = "재고", example = "100")
    @NotNull(message = "재고를 적어주세요")
    @Max(value = 20000, message = "재고 입력값이 최대값을 초과했습니다")
    private Integer stock;
}
