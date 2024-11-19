package com.sparta.oneeat.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryReqDto {

    @NotBlank
    private String category;

}
