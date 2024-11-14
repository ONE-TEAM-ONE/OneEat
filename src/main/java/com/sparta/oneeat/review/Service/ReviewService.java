package com.sparta.oneeat.review.Service;

import com.sparta.oneeat.review.dto.*;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ReviewService {
    // 리뷰 생성
    CreateReviewResDto createReview(long userId, UUID orderId, CreateReviewReqDto createReviewReqDto);

    // 가게 리뷰 목록 조회
    Page<ReviewListDto> getReviewList(UUID storeId, int page, int size, String sort, boolean isAsc);

    // 리뷰 수정
    ModifyReviewResDto modifyReview(long userId, UUID orderId, UUID reviewId, ModifyReviewReqDto modifyReviewReqDto);

    // 리뷰 숨김(논리적 삭제)
    void softDeleteReview(long userId, UUID reviewId);

    // 리뷰 삭제
    void hardDeleteReview(long userId, UUID reviewId);
}
