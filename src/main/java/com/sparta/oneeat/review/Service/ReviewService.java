package com.sparta.oneeat.review.Service;

import com.sparta.oneeat.review.dto.CreateReviewReqDto;
import com.sparta.oneeat.review.dto.CreateReviewResDto;
import com.sparta.oneeat.review.dto.ReviewListDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ReviewService {
    // 리뷰 생성
    CreateReviewResDto createReview(long userId, UUID orderId, CreateReviewReqDto createReviewReqDto);

    // 가게 리뷰 목록 조회
    Page<ReviewListDto> getReviewList(UUID storeId, int page, int size, String sort, boolean isAsc);
}
