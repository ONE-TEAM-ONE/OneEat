package com.sparta.oneeat.order.dto;

import com.sparta.oneeat.order.entity.Order;
import com.sparta.oneeat.order.entity.OrderMenu;
import com.sparta.oneeat.order.entity.OrderTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderDetailDto {

    // 주문 타입
    private OrderTypeEnum type;
    // 배송지
    private String address;
    // 메뉴 목록
    private List<OrderMenuDto> menuList = new ArrayList<>();

    public OrderDetailDto(Order order) {
        this.type = order.getType();
        this.address = order.getAddress();
        for(OrderMenu orderMenu : order.getOrderMenuList()){
            menuList.add(new OrderMenuDto(orderMenu));
        }
    }
}
