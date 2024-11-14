package com.sparta.oneeat.review.controller;

import com.sparta.oneeat.common.response.BaseResponseBody;
import com.sparta.oneeat.review.Service.ReviewService;
import com.sparta.oneeat.review.dto.CreateReviewReqDto;
import com.sparta.oneeat.review.dto.ModifyReviewReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Tag(name="Review", description="Review API")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 생성", description = "고객이 자신의 주문에 대한 리뷰를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "리뷰 등록 성공"),
            @ApiResponse(responseCode = "500", description = "리뷰 등록 실패")
    })
    @PostMapping("/order/{order_id}/review")
    public ResponseEntity<? extends BaseResponseBody> createReview(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable(name = "order_id") UUID orderId,
            @Validated @RequestBody CreateReviewReqDto createReviewReqDto
    ){

        log.info("orderId : {}", orderId);
        log.info("rating : {}", createReviewReqDto.getRating());
        log.info("content : {}", createReviewReqDto.getContent());

        return ResponseEntity.status(201).body(BaseResponseBody.of(0, reviewService.createReview(2L, orderId, createReviewReqDto)));
    }

    @Operation(summary = "리뷰 조회", description = "가게의 리뷰를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 조회 성공"),
            @ApiResponse(responseCode = "500", description = "리뷰 조회 실패")
    })
    @GetMapping("/store/{store_id}/reviews")
    public ResponseEntity<? extends BaseResponseBody> getReviewList(
            @PathVariable(name = "store_id") UUID storeId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String sort,
            @RequestParam("isAsc") boolean isAsc
    ){

        log.info("storeId : {}", storeId);
        log.info("page : {}", page);
        log.info("size : {}", size);
        log.info("sort : {}", sort);
        log.info("isAsc : {}", isAsc);

        return ResponseEntity.status(200).body(BaseResponseBody.of(0, reviewService.getReviewList(storeId, (page-1), size, sort, isAsc)));
    }

    @Operation(summary = "리뷰 수정", description = "자신이 작성한 리뷰를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 수정 성공"),
            @ApiResponse(responseCode = "500", description = "리뷰 수정 실패")
    })
    @PutMapping("/order/{order_id}/review/{review_id}")
    public ResponseEntity<? extends BaseResponseBody> modifyReview(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable(name = "order_id") UUID orderId,
            @PathVariable(name = "review_id") UUID reviewId,
            @Validated @RequestBody ModifyReviewReqDto modifyReviewReqDto
            ){

        log.info("orderId : {}", orderId);
        log.info("reviewId : {}", reviewId);

        return ResponseEntity.status(200).body(BaseResponseBody.of(0, reviewService.modifyReview(3L, orderId, reviewId, modifyReviewReqDto)));
    }

    @Operation(summary = "리뷰 숨김(논리적 삭제)", description = "리뷰를 숨김(논리적 삭제)를 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 숨김 성공"),
            @ApiResponse(responseCode = "500", description = "리뷰 숨김 실패")
    })
    @PatchMapping("/order/{order_id}/review/{review_id}")
    public ResponseEntity<? extends BaseResponseBody> softDeleteReview(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable(name = "review_id") UUID reviewId
    ){

        log.info("reviewId : {}", reviewId);

        reviewService.softDeleteReview(3L, reviewId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(0, null));
    }

    @Operation(summary = "리뷰 삭제", description = "숨김 처리된 리뷰를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 삭제 성공"),
            @ApiResponse(responseCode = "500", description = "리뷰 삭제 실패")
    })
    @DeleteMapping("/order/{order_id}/review/{review_id}")
    public ResponseEntity<? extends BaseResponseBody> hardDeleteReview(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable(name = "review_id") UUID reviewId
    ){

        log.info("reviewId : {}", reviewId);

        reviewService.hardDeleteReview(2L, reviewId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(0, null));
    }
}
