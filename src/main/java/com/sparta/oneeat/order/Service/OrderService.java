package com.sparta.oneeat.order.Service;

import com.sparta.oneeat.order.dto.OrderListDto;
import org.springframework.data.domain.Page;

public interface OrderService {

    // 주문 리스트 조회
    Page<OrderListDto> getOrderList(long userId, int page, int size, String sort, boolean isAsc);
}
