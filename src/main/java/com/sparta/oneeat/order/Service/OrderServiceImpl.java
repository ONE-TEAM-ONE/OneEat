package com.sparta.oneeat.order.Service;

import com.sparta.oneeat.common.exception.CustomException;
import com.sparta.oneeat.common.exception.ExceptionType;
import com.sparta.oneeat.order.dto.OrderDetailDto;
import com.sparta.oneeat.order.dto.OrderListDto;
import com.sparta.oneeat.order.entity.Order;
import com.sparta.oneeat.order.entity.OrderStatusEnum;
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

import java.util.Objects;
import java.util.UUID;

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

    @Override
    public OrderDetailDto getOrderDetail(long userId, UUID orderId) {
        // 유저확인
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.INTERNAL_SERVER_ERROR));
        // 주문 확인
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(ExceptionType.ORDER_NOT_EXIST));

        OrderDetailDto orderDetailDto = null;

        if(user.getRole() == UserRoleEnum.CUSTOMER){
            // 고객인 경우, 해당 주문을 한 고객인지 확인

            log.info(" 유저 엔티티 유저 PK : {}", user.getId());
            log.info(" 주문 엔티티 유저 FK : {}", order.getUser().getId());

            if(!Objects.equals(user.getId(), order.getUser().getId())) throw new CustomException(ExceptionType.ACCESS_DENIED);

            orderDetailDto = new OrderDetailDto(order);

        }else if(user.getRole() == UserRoleEnum.OWNER){
            // 주인인 경우, 해당 가게의 주문인지 확인

            log.info(" 유저 엔티티 상품 FK : {}", user.getStoreList().get(0).getId());
            log.info(" 주문 엔티티 상품 FK : {}", order.getStore().getId());

            if(!Objects.equals(user.getStoreList().get(0).getId(), order.getStore().getId())) throw new CustomException(ExceptionType.ACCESS_DENIED);

            orderDetailDto = new OrderDetailDto(order);
        }

        return orderDetailDto;
    }

    @Override
    @Transactional
    public void cancelOrder(long userId, UUID orderId) {
        // 유저확인
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.INTERNAL_SERVER_ERROR));
        // 주문 확인
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(ExceptionType.ORDER_NOT_EXIST));

        // 해당 주문의 고객이 아닌 경우 예외 처리
        if(!Objects.equals(user.getId(), order.getUser().getId())) throw new CustomException(ExceptionType.ACCESS_DENIED);


        OrderStatusEnum currentStatus = order.getStatus();

        // 이미 취소가 된 주문이라면 예외처리
        if(currentStatus == OrderStatusEnum.PAYMENT_CANCELLED) throw new CustomException(ExceptionType.ALREADY_CANCELLED);

        if(currentStatus == OrderStatusEnum.PAYMENT_PENDING || currentStatus == OrderStatusEnum.PAYMENT_APPROVED) {
            // 현재 상태가 결제 대기 또는 결제 승인일 경우에만 취고 가능
            order.cancle();
        }else{
            // 다른 상태일 때 예외처리
            throw new CustomException(ExceptionType.CANCLE_NOT_ALLOW);
        }
    }

}
