package com.allforone.starvestop.domain.user.entity;

import com.allforone.starvestop.common.entity.BaseEntity;
import com.allforone.starvestop.domain.user.enums.AuthProvider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

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


    @Column(nullable = false, length = 15)
    private String nickname;

    @Column(nullable = false, length = 30)
    private String username;

    //유저 고유키 ( 자동 결제에 필요한 키 )
    @Column(nullable = false, unique = true, updatable = false, length = 36)
    private String userKey;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private Long providerId;

    private String imageUuid;

    private User(String email, String password, String nickname, String username, AuthProvider provider, Long providerId) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.username = username;
        this.provider = provider;
        this.providerId = providerId;
    }

    @PrePersist
    private void generateUserKey() {
        if (this.userKey == null) {
            this.userKey = UUID.randomUUID().toString();
        }
    }

    public static User create(String email, String password, String nickname, String username) {
        return new User(email, password, nickname, username, AuthProvider.LOCAL, null);
    }

    public static User createKakao(String email, String password, String nickname, String username, Long providerId) {
        return new User(email, password, nickname, username, AuthProvider.KAKAO, providerId);
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateOAuth(String nickname) {
        this.nickname = nickname;
    }

    public void uploadImageUrl(String uuid) {
        this.imageUuid = uuid;
    }
}
