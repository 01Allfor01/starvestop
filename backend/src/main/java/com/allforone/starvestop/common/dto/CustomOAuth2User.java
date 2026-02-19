package com.allforone.starvestop.common.dto;

import com.allforone.starvestop.common.enums.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class CustomOAuth2User implements OAuth2User {
    private final Long userId;
    private final String username;
    private final String email;
    private final UserRole role;
    private final Map<String, Object> attributes;
    public CustomOAuth2User(String username, String email, Long id, UserRole role, Map<String, Object> attributes) {
        this.userId = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.attributes = attributes;
    }

    @Nullable
    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getName() {
        return String.valueOf(userId);
    }
}
