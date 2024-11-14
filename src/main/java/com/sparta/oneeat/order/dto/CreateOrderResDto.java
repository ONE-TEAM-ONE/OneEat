package com.sparta.oneeat.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateOrderResDto {

    private UUID orderId;

    public CreateOrderResDto(UUID orderId) {
        this.orderId = orderId;
    }
}
