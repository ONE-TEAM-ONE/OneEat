package com.sparta.oneeat.payment.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreatePaymentResDto {

    private UUID paymentId;

    public CreatePaymentResDto(UUID paymentId) {
        this.paymentId = paymentId;
    }
}
