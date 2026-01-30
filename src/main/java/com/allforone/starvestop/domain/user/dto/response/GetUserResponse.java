package com.allforone.starvestop.domain.user.dto.response;

import com.allforone.starvestop.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserResponse {
    private final Long id;
    private final String email;
    private final String nickname;
    private final String username;
    private final String imageUrl;

    public static GetUserResponse from(User user, String imageUrl) {
        return new GetUserResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getUsername(),
                imageUrl
        );
    }
}