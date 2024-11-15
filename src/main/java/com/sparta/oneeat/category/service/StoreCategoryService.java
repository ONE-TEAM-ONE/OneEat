package com.sparta.oneeat.category.service;

import com.sparta.oneeat.category.dto.CreateCategoryReqDto;
import com.sparta.oneeat.category.dto.CreateCategoryResDto;
import com.sparta.oneeat.category.dto.UpdateCategoryReqDto;
import com.sparta.oneeat.user.entity.User;
import java.util.UUID;

public interface StoreCategoryService {
    CreateCategoryResDto createCategory(Long id, CreateCategoryReqDto createCategoryReqDto);

    void updateCategory(User user, UpdateCategoryReqDto createCategoryReqDto, UUID categoryId);
  
    void deleteCategory(User user, UUID categoryId);
}
