package com.sparta.oneeat.user.entity;

import com.sparta.oneeat.store.entity.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="P_USERS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USERS_ID", nullable = false)
    private Long id;

    @OneToMany(mappedBy = "user")
    private List<Store> storeList;

    @OneToMany(mappedBy = "user")
    @JoinColumn(name="USER_ADDRESS_ID", nullable = false)
    private List<UserAddress> userAddress;

    @Column(name="USERS_USERNAME", nullable = false)
    private String name;

    @Column(name="USERS_PASSWORD", nullable = false)
    private String password;

    @Column(name="USERS_NICKNAME")
    private String nickname;

    @Column(name="USERS_EMAIL", nullable = false)
    private String email;

    @Column(name="USERS_ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;
}
