package com.sparta.oneeat.review.controller;

import com.sparta.oneeat.common.response.BaseResponseBody;
import com.sparta.oneeat.review.Service.ReviewService;
import com.sparta.oneeat.review.dto.CreateReviewReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
@Tag(name="Review", description="Review API")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 생성", description = "고객이 자신의 주문에 대한 리뷰를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "리뷰 등록 성공"),
            @ApiResponse(responseCode = "500", description = "리뷰 등록 실패")
    })
    @PostMapping("/{order_id}/review")
    public ResponseEntity<? extends BaseResponseBody> createReview(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable(name = "order_id") UUID orderId,
            @RequestBody CreateReviewReqDto createReviewReqDto
    ){

        log.info("orderId : {}", orderId);
        log.info("rating : {}", createReviewReqDto.getRating());
        log.info("content : {}", createReviewReqDto.getContent());

        return ResponseEntity.status(201).body(BaseResponseBody.of(0, reviewService.createReview(2L, orderId, createReviewReqDto)));
    }

}
