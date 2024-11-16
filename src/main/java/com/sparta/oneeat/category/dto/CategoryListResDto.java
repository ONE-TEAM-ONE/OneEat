package com.sparta.oneeat.category.dto;

import com.sparta.oneeat.category.entity.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CategoryListResDto {

    private UUID categoryId;
    private String name;

    public CategoryListResDto(Category category){
        this.categoryId = category.getId();
        this.name = category.getCategoryName();
    }
}
