package com.sparta.oneeat.payment.controller;

import com.sparta.oneeat.common.response.BaseResponseBody;
import com.sparta.oneeat.payment.dto.ModifyPaymentStatusDto;
import com.sparta.oneeat.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Tag(name="Payment", description="Payment API")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "결제 생성", description = "결제 정보를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "결제 생성 성공"),
            @ApiResponse(responseCode = "500", description = "결제 생성 실패")
    })
    @PostMapping("/{order_id}/payment")
    public ResponseEntity<? extends BaseResponseBody> createPayment(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable(name = "order_id") UUID orderId
    ){

        log.info("orderId : {}", orderId);

        return ResponseEntity.status(200).body(BaseResponseBody.of(0, paymentService.createPayment(3L, orderId)));

    }

    @Operation(summary = "결제 상태 변경", description = "결제 상태를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "결제 상태 수정 성공"),
            @ApiResponse(responseCode = "500", description = "결제 상태 수정 실패")
    })
    @PutMapping("/{order_id}/payment/{payment_id}")
    public ResponseEntity<? extends BaseResponseBody> modifyPaymentStatus(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable(name = "order_id") UUID orderId,
            @PathVariable(name = "payment_id") UUID paymentId,
            @Validated  @RequestBody ModifyPaymentStatusDto modifyPaymentStatusDto
    ){

        log.info("paymentId : {}", paymentId);
        log.info("status : {}", modifyPaymentStatusDto.getStatus());

        paymentService.modifyPaymentStatus(2L, orderId, paymentId, modifyPaymentStatusDto);
        return ResponseEntity.status(200).body(BaseResponseBody.of(0, null));

    }

}
