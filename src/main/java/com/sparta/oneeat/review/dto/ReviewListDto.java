package com.sparta.oneeat.review.dto;

import com.sparta.oneeat.review.entity.Review;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ReviewListDto {

    // 리뷰 PK
    private UUID reviewId;
    // 주문 PK
    private UUID orderId;
    // 닉네임
    private String nickname;
    // 내용
    private String content;
    // 별점
    private int rating;
    // 이미지
    private String image;

    public ReviewListDto(Review review){
        this.reviewId = review.getId();
        this.orderId = review.getOrder().getId();
        this.nickname = review.getUser().getNickname();
        this.content = review.getContent();
        this.rating = review.getRating();
        this.image = review.getImage();
    }
}
