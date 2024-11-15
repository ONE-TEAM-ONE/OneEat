package com.sparta.oneeat.store.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Entity
@Table(name="P_DELIVERY_REGION")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryRegion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="DELIVERY_REGION_ID", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_ID", nullable = false)
    private Store store;

    @Column(name = "DELIVERY_REGION")
    private String deliveryRegion;
}
