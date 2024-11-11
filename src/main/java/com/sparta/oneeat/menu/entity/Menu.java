package com.sparta.oneeat.menu.entity;

import com.sparta.oneeat.order.entity.OrderMenu;
import com.sparta.oneeat.store.entity.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "P_ITEM")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="ITEM_ID", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "STORE_ID")
    private Store store;

    @OneToMany(mappedBy = "menu")
    private List<OrderMenu> orderMenuList;

    @Column(name="ITEM_NAME")
    private String name;

    @Column(name="ITEM_DESC")
    private String description;

    @Column(name="ITEM_AI")
    private String ai;

    @Column(name="ITEM_PRICE")
    private Integer price;

    @Column(name="ITEM_IMAGE")
    private String image;

    @Column(name="ITEM_STATUS")
    private String status;
}
