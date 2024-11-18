package com.sparta.oneeat.user.entity;

import com.sparta.oneeat.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="P_USER_ADDRESS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAddress extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="USER_ADDRESS_ID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERS_ID")
    private User user;

    @Column(name="USER_ADDRESS_DATAIL")
    private String address;

    public UserAddress(User user, String address) {
        this.user = user;
        this.address = address;
    }

    public void modifyAddress(String address) {
        this.address = address;
    }

    public void softDelete(Long userId) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = userId;
    }

}
