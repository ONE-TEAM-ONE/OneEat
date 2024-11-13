package com.sparta.oneeat.order.dto;

import com.sparta.oneeat.order.entity.Order;
import com.sparta.oneeat.order.entity.OrderStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class OrderListDto {
    private UUID orderId;
    private String storeName;
    private int totalPrice;
    private OrderStatusEnum status;
    private LocalDateTime orderDate;

    public OrderListDto(Order order){
        this.orderId = order.getId();
        this.storeName = order.getStore().getName();
        this.totalPrice = order.getTotalPrice();
        this.status = order.getStatus();
        this.orderDate = order.getCreatedAt();
    }
}
