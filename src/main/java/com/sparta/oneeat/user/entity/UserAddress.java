package com.sparta.oneeat.user.entity;

import com.sparta.oneeat.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name = "USERS_ID")
    private User user;

//    @ManyToOne
//    @JoinColumn(name = "ADDRESS_CODE_ID", nullable = false)
//    private AddressCode addressCode;

    @Column(name="USER_ADDRESS_DATAIL")
    private String address;

    public UserAddress(User user, String address) {
        this.user = user;
        this.address = address;
    }
}
