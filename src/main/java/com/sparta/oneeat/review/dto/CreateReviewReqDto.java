package com.sparta.oneeat.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReviewReqDto {

    private int rating;
    private String content;
    private String image;

}
