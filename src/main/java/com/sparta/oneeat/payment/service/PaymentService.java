package com.sparta.oneeat.payment.service;

import com.sparta.oneeat.payment.dto.CreatePaymentResDto;

import java.util.UUID;

public interface PaymentService {

    // 결제 생성
    CreatePaymentResDto createPayment(long userId, UUID orderId);
}
