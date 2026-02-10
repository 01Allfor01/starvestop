package com.allforone.starvestop.domain.owner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateOwnerResponse {
    private final String email;
    private final String userName;
}
