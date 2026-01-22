package com.allforone.starvestop.domain.user.entity;

import com.allforone.starvestop.common.entity.BaseEntity;
import com.allforone.starvestop.domain.user.enums.AuthProvider;
import com.allforone.starvestop.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false, length = 15)
    private String nickname;

    @Column(nullable = false, length = 30)
    private String username;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    private User(String email, String password, UserRole role, String nickname, String username, AuthProvider provider, String providerId) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.nickname = nickname;
        this.username = username;
        this.provider = provider;
        this.providerId = providerId;
    }

    public static User create(String email, String password, UserRole role, String nickname, String username) {
        return new User(email, password, role, nickname, username, AuthProvider.LOCAL, null);
    }

    public static User createKakao(String email, String password, UserRole role, String nickname, String username, String providerId) {
        return new User(email, password, role, nickname, username, AuthProvider.KAKAO, providerId);
    }

    public void update(String nickname, String password, UserRole userRole) {
        this.nickname = nickname;
        this.password = password;
        this.role = userRole;
    }

    public void updateOAuth(String nickname, UserRole userRole) {
        this.nickname = nickname;
        this.role = userRole;
    }
}
