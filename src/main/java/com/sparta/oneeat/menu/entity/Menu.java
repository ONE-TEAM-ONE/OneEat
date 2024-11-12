package com.sparta.oneeat.menu.entity;

import com.sparta.oneeat.store.entity.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name="ITEM_NAME", nullable = false)
    private String name;

    @Column(name="ITEM_DESC")
    private String description;

    @Column(name="ITEM_AI", nullable = false)
    private Boolean ai;

    @Column(name="ITEM_PRICE", nullable = false)
    private Integer price;

    @Column(name="ITEM_IMAGE")
    private String image;

    @Column(name="ITEM_STATUS")
    @Enumerated(EnumType.STRING)
    private MenuStatusEnum status;
}
