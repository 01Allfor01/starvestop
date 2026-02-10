package com.allforone.starvestop.domain.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendMessageRequest {

    @NotBlank(message = "메시지를 입력해 주세요")
    @Size(max = 1000, message = "메시지는 최대 1000자까지 입력 가능합니다.")
    private String content;
}
