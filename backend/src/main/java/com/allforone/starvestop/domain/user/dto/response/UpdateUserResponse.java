package com.allforone.starvestop.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateUserResponse {
    private final String email;
    private final String nickname;
    private final String username;
}
