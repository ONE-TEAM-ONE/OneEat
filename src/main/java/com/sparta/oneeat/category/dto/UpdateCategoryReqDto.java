package com.sparta.oneeat.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCategoryReqDto {

    @NotBlank(message = "카테고리명은 필수 입력입니다.")
    private String categoryName;
}
