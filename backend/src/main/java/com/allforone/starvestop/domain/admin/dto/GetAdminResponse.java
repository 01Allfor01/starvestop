package com.allforone.starvestop.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetAdminResponse {
    private final Long id;
    private final String email;

    public static GetAdminResponse from(Long id, String email) {
        return new GetAdminResponse(id, email);
    }
}
