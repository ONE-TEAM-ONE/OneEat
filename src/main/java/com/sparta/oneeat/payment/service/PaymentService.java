package com.sparta.oneeat.payment.service;

import com.sparta.oneeat.payment.dto.CreatePaymentResDto;
import com.sparta.oneeat.payment.dto.ModifyPaymentStatusDto;

import java.util.UUID;

public interface PaymentService {

    // 결제 생성
    CreatePaymentResDto createPayment(long userId, UUID orderId);

    // 상태 변경
    void modifyPaymentStatus(long userId, UUID orderId, UUID paymentId, ModifyPaymentStatusDto modifyPaymentStatusDto);
}
