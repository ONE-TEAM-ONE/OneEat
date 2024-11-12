package com.sparta.oneeat.order.dto;

import com.sparta.oneeat.order.entity.OrderMenu;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderMenuDto {

    private UUID menuId;
    private String menuName;
    private int amount;
    private int pricePerMenu;

    public OrderMenuDto(OrderMenu orderMenu) {
        this.menuId = orderMenu.getMenu().getId();
        this.menuName = orderMenu.getMenu().getName();
        this.amount = orderMenu.getQuantity();
        this.pricePerMenu = orderMenu.getPrice() * orderMenu.getQuantity();
    }
}
