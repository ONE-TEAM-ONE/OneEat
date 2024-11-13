package com.sparta.oneeat.review.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateReviewResDto {

    private UUID reviewId;

    public CreateReviewResDto(UUID reviewId){
        this.reviewId = reviewId;
    }

}
