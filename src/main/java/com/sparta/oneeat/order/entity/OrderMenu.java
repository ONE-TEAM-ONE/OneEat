package com.sparta.oneeat.order.entity;

import com.sparta.oneeat.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "P_MENU_ORDER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderMenu {
    @ManyToOne
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID", nullable = false)
    private Menu menu;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="MENU_ORDER_ID", nullable = false)
    private UUID id;

    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

    @Column(name = "PRICE", nullable = false)
    private Integer price;

}
