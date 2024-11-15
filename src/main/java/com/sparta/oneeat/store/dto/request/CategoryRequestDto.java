package com.sparta.oneeat.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequestDto {

    @NotBlank(message = "카테고리명은 필수 입력입니다.")
    private String categoryName;
}
