package com.allforone.starvestop.domain.user.dto.response;

import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.enums.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserResponse {
    private final Long id;
    private final String email;
    private final String nickname;
    private final String username;
    private final String userKey;
    private final AuthProvider authProvider;
    private final String imageUrl;

    public static GetUserResponse from(User user, String imageUrl) {
        return new GetUserResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getUsername(),
                user.getUserKey(),
                user.getProvider(),
                imageUrl
        );
    }
}