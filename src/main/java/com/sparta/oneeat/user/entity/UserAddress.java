package com.sparta.oneeat.user.entity;

import com.sparta.oneeat.store.entity.AddressCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="P_USER_ADDRESS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="USER_ADDRESS_ID", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "USERS_ID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "ADDRESS_CODE_ID", nullable = false)
    private AddressCode addressCode;

    @Column(name="USER_ADDRESS_DATAIL")
    private String detail;
}
