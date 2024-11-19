package com.sparta.oneeat.payment.dto;

import com.sparta.oneeat.payment.entity.PaymentStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModifyPaymentStatusDto {

    @NotNull(message = "변경 할 상태값을 반드시 넣어야 합니다.")
    private PaymentStatusEnum status;

}
