package com.sparta.oneeat.store.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="P_DELIVERY_REGION")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryRegion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="DELIVERY_REGION_ID", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "STORE_ID", nullable = false)
    private Store store;

    @ManyToOne
    @JoinColumn(name = "ADDRESS_CODE_ID", nullable = false)
    private AddressCode addressCode;

}
