package com.sparta.oneeat.payment.service;

import com.sparta.oneeat.common.exception.CustomException;
import com.sparta.oneeat.common.exception.ExceptionType;
import com.sparta.oneeat.order.entity.Order;
import com.sparta.oneeat.order.entity.OrderStatusEnum;
import com.sparta.oneeat.order.repository.OrderRepository;
import com.sparta.oneeat.payment.dto.CreatePaymentResDto;
import com.sparta.oneeat.payment.dto.ModifyPaymentStatusDto;
import com.sparta.oneeat.payment.entity.Payment;
import com.sparta.oneeat.payment.entity.PaymentStatusEnum;
import com.sparta.oneeat.payment.repository.PaymentRepository;
import com.sparta.oneeat.user.entity.User;
import com.sparta.oneeat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService{

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public CreatePaymentResDto createPayment(long userId, UUID orderId) {

        // 유저 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.INTERNAL_SERVER_ERROR));

        // 주문 조회
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(ExceptionType.ORDER_NOT_EXIST));

        // 해당 고객의 주문인지 확인
        if(!Objects.equals(user.getId(), order.getUser().getId())) throw new CustomException(ExceptionType.ACCESS_DENIED);

        Payment saved = paymentRepository.save(new Payment(order));

        return new CreatePaymentResDto(saved.getId());
    }

    @Override
    @Transactional
    public void modifyPaymentStatus(long userId, UUID orderId, UUID paymentId, ModifyPaymentStatusDto modifyPaymentStatusDto) {
        // 유저 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.INTERNAL_SERVER_ERROR));

        // 주문 조회
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(ExceptionType.ORDER_NOT_EXIST));

        // 해당 고객의 주문인지 확인
        if(!Objects.equals(user.getId(), order.getUser().getId())) throw new CustomException(ExceptionType.ACCESS_DENIED);

        // 결제 조회
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new CustomException(ExceptionType.PAYMENT_NOT_EXIST));

        // 해당 주문의 결제인지 확인
        if(!Objects.equals(order.getId(), payment.getOrder().getId())) throw new CustomException(ExceptionType.ORDER_PAYMENT_MISMATCHED);

        // 이미 처리된 결제라면 예외처리
        if(payment.getStatus() != PaymentStatusEnum.NOT_PAID) throw new CustomException(ExceptionType.PAYMENT_ALREADY_PROCESSED);

        // 수정
        payment.modifyStatus(modifyPaymentStatusDto);

        // 주문 상태도 같이 반영
        OrderStatusEnum orderStatus = (modifyPaymentStatusDto.getStatus() == PaymentStatusEnum.SUCCESS) ? OrderStatusEnum.PAYMENT_APPROVED : OrderStatusEnum.PAYMENT_REJECTED;
        order.modifyStatus(orderStatus);
    }
}
