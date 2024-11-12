package com.sparta.oneeat.order.Service;

import com.sparta.oneeat.common.exception.CustomException;
import com.sparta.oneeat.common.exception.ExceptionType;
import com.sparta.oneeat.order.dto.OrderListDto;
import com.sparta.oneeat.order.entity.Order;
import com.sparta.oneeat.order.repository.OrderRepository;
import com.sparta.oneeat.store.entity.Store;
import com.sparta.oneeat.store.repository.StoreRepository;
import com.sparta.oneeat.user.entity.User;
import com.sparta.oneeat.user.entity.UserRoleEnum;
import com.sparta.oneeat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Override
    public Page<OrderListDto> getOrderList(long userId, int page, int size, String sort, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort1 = Sort.by(direction, sort);
        Pageable pageable = PageRequest.of(page, size, sort1);

        // 유저 권한 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.INTERNAL_SERVER_ERROR));
        UserRoleEnum userRole = user.getRole();

        Page<Order> orderList = null;

        if(userRole == UserRoleEnum.CUSTOMER){
            // 고객일 경우 해당 고객의 주문 리스트 조회
            orderList = orderRepository.findAllByUser(user, pageable);
        }else if(userRole == UserRoleEnum.OWNER){
            // 가게 주인일 경우 해당 가게의 주문 리스트 조회
            Store store = storeRepository.findByUser(user).orElseThrow(() -> new CustomException(ExceptionType.INTERNAL_SERVER_ERROR));
            orderList = orderRepository.findAllByStore(store, pageable);
        }
        
        return orderList.map(OrderListDto::new);
    }

}
