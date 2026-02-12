package com.allforone.starvestop.domain.admin.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "admins")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private Boolean isDeleted;

    private Admin(String email, String password) {
        this.email = email;
        this.password = password;
        this.isDeleted = false;
    }

    public static Admin create(String email, String password) {
        return new Admin(email, password);
    }

    public void update(String password) {
        this.password = password;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
