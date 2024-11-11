package com.sparta.oneeat.menu.entity;

import com.sparta.oneeat.order.entity.OrderMenu;
import com.sparta.oneeat.store.entity.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "P_ITEM")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="ITEM_ID", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "STORE_ID")
    private Store store;

    @Column(name="ITEM_NAME")
    private String name;

    @Column(name="ITEM_DESC")
    private String description;

    @Column(name="ITEM_AI")
    private Boolean ai;

    @Column(name="ITEM_PRICE")
    private Integer price;

    @Column(name="ITEM_IMAGE")
    private String image;

    @Column(name="ITEM_STATUS")
    @Enumerated(EnumType.STRING)
    private MenuStatusEnum status;
}
