package com.sparta.oneeat.order.controller;

import com.sparta.oneeat.common.response.BaseResponseBody;
import com.sparta.oneeat.order.Service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Tag(name="Order", description="Order API")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문 목록 조회", description = "자신의 주문 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 목록 조회 성공"),
            @ApiResponse(responseCode = "500", description = "주문 목록 조회 실패")
    })
    @GetMapping("")
    public ResponseEntity<? extends BaseResponseBody> getOrderList(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String sort,
            @RequestParam("isAsc") boolean isAsc
    ){

        log.info("page : {}", page);
        log.info("size : {}", size);
        log.info("sort : {}", sort);
        log.info("isAsc : {}", isAsc);

        return ResponseEntity.status(200).body(BaseResponseBody.of(0, orderService.getOrderList(2L, (page-1), size, sort, isAsc)));
    }


    @Operation(summary = "주문 상세 조회", description = "주문 내역을 상세 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 상세 조회 성공"),
            @ApiResponse(responseCode = "500", description = "주문 상세 조회 실패")
    })
    @GetMapping("/{order_id}")
    public ResponseEntity<? extends BaseResponseBody> getOrderDetail(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable(name = "order_id") UUID orderId
    ){

        log.info("orderId : {}", orderId);

        return ResponseEntity.status(200).body(BaseResponseBody.of(0, orderService.getOrderDetail(1L, orderId)));
    }

}


