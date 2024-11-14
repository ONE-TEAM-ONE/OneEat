package com.sparta.oneeat.review.dto;

import com.sparta.oneeat.review.entity.Review;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ModifyReviewResDto {

    private UUID reviewId;
    private int rating;
    private String content;
    private String image;

    public ModifyReviewResDto(Review review) {
        this.reviewId = review.getId();
        this.rating = review.getRating();
        this.content = review.getContent();
        this.image = review.getImage();
    }
}
