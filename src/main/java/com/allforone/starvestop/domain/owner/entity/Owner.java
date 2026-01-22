package com.allforone.starvestop.domain.owner.entity;

import com.allforone.starvestop.common.entity.BaseEntity;
import com.allforone.starvestop.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "owners")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Owner extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    private Owner(String email, String password, UserRole role, String userName){
        this.email = email;
        this.password = password;
        this.role = role;
        this.username = userName;
    }

    public static Owner create(String email, String password, UserRole role, String userName) {
        return new Owner(
                email,
                password,
                role,
                userName
        );
    }

    public void update(String password) {
        this.password = password;
    }
}
