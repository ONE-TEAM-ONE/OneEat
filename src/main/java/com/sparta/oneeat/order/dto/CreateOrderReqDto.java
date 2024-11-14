package com.sparta.oneeat.order.dto;

import com.sparta.oneeat.order.entity.OrderTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class CreateOrderReqDto {

    @NotNull(message = "가게를 입력해야합니다.")
    private UUID storeId;

    @NotNull(message = "주문 타입을 입력해야합니다.")
    private OrderTypeEnum type;

    @NotBlank(message = "배송지를 입력해야합니다.")
    private String address;

    @NotNull(message = "메뉴 목록을 입력해야합니다.")
    @Size(min = 1, message = "메뉴 목록에는 최소 한 개의 항목이 있어야 합니다.")
    private List<MenuDto> menuList;

    @Getter
    public static class MenuDto {
        private UUID menuId;
        private int amount;
        private int price;
    }
}
