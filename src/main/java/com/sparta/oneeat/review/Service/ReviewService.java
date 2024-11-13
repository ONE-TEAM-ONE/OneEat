package com.sparta.oneeat.review.Service;

import com.sparta.oneeat.review.dto.CreateReviewReqDto;
import com.sparta.oneeat.review.dto.CreateReviewResDto;

import java.util.UUID;

public interface ReviewService {
    // 리뷰 생성
    CreateReviewResDto createReview(long userId, UUID orderId, CreateReviewReqDto createReviewReqDto);
}
