package com.sparta.oneeat.payment.controller;

import com.sparta.oneeat.common.response.BaseResponseBody;
import com.sparta.oneeat.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        return ResponseEntity.status(200).body(BaseResponseBody.of(0, paymentService.createPayment(2L, orderId)));

    }

}
