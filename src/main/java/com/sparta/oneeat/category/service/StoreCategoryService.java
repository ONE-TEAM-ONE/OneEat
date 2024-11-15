package com.sparta.oneeat.category.service;

import com.sparta.oneeat.category.dto.CreateCategoryReqDto;
import com.sparta.oneeat.category.dto.CreateCategoryResDto;

public interface StoreCategoryService {
    CreateCategoryResDto createCategory(Long id, CreateCategoryReqDto createCategoryReqDto);
}
