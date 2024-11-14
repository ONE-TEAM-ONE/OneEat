package com.sparta.oneeat.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModifyReviewReqDto {

    @Min(value = 0, message = "별점은 0~5점 사이만 가능합니다.")
    @Max(value = 5, message = "별점은 0~5점 사이만 가능합니다.")
    private int rating;

    @NotBlank(message = "리뷰 내용을 적어주세요.")
    private String content;

    private String image;
}
