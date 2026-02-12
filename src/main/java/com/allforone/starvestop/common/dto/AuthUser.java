package com.allforone.starvestop.common.dto;

import com.allforone.starvestop.common.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthUser {

    private final Long userId;
    private final String email;
    private final String username;
    private final UserRole userRole;

    public static AuthUser of(Long userId, String email, String username, UserRole userRole) {
        return new AuthUser(userId, email, username, userRole);
    }
}