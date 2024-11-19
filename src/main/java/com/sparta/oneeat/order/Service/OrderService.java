package com.sparta.oneeat.order.Service;

import com.sparta.oneeat.order.dto.CreateOrderReqDto;
import com.sparta.oneeat.order.dto.CreateOrderResDto;
import com.sparta.oneeat.order.dto.OrderDetailDto;
import com.sparta.oneeat.order.dto.OrderListDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface OrderService {

    // 주문 리스트 조회
    Page<OrderListDto> getOrderList(long userId, int page, int size, String sort, boolean isAsc);

    // 주문 상세 조회
    OrderDetailDto getOrderDetail(long userId, UUID orderId);

    // 주문 취소
    void cancelOrder(long userId, UUID orderId);

    // 주문 상태 변경
    void modifyOrderStatus(long userId, UUID orderId);

    // 주문 생성
    CreateOrderResDto createOrder(Long userId, CreateOrderReqDto createOrderReqDto);
}
