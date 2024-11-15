package com.sparta.oneeat.user.entity;

import com.sparta.oneeat.common.entity.BaseEntity;
import com.sparta.oneeat.store.entity.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "P_USERS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USERS_ID", nullable = false)
    private Long id;

    @Column(name = "USERS_ADDRESS", nullable = false)
    private String currentAddress;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Store> storeList;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserAddress> userAddress;

    @Column(name = "USERS_USERNAME", nullable = false)
    private String name;

    @Column(name = "USERS_PASSWORD", nullable = false)
    private String password;

    @Column(name = "USERS_NICKNAME")
    private String nickname;

    @Column(name = "USERS_EMAIL", nullable = false)
    private String email;

    @Column(name = "USERS_ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    @Builder
    public User(String name, String password, String nickname, String email, String currentAddress) {
        this.name = name;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.currentAddress = currentAddress;
        this.role = UserRoleEnum.CUSTOMER;
        this.userAddress = new ArrayList<>();
        this.storeList = new ArrayList<>();
    }

    public void modifyPassword(String password) {
        this.password = password;
    }

    public void modifyNickname(String nickname) {
        this.nickname = nickname;
    }

    public void modifyEmail(String email) { this.email = email; }

    public void softDelete(long id) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = id;
    }
}