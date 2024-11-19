package com.sparta.oneeat.order.entity;

public enum OrderStatusEnum {
    PAYMENT_PENDING,       // 결제 대기
    PAYMENT_APPROVED,      // 결제 승인
    PAYMENT_REJECTED,      // 결제 거절
    PAYMENT_CANCELLED,     // 결제 취소
    COOKING,               // 요리중
    DELIVERING,            // 배달중
    DELIVERY_COMPLETED     // 배달완료
}
