package com.allforone.starvestop.domain.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "채팅 메시지 전송 요청")
@Getter
@NoArgsConstructor
public class SendMessageRequest {

    @Schema(description = "전송할 메시지 내용", example = "안녕하세요!")
    @NotBlank(message = "메시지를 입력해 주세요")
    @Size(max = 1000, message = "메시지는 최대 1000자까지 입력 가능합니다.")
    private String content;
}
