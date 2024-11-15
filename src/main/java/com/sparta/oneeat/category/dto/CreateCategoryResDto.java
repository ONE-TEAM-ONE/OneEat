package com.sparta.oneeat.category.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateCategoryResDto {

    private UUID categoryId;

    public CreateCategoryResDto(UUID categoryId) {
        this.categoryId = categoryId;
    }
}
