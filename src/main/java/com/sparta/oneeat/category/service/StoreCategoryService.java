package com.sparta.oneeat.category.service;

import com.sparta.oneeat.category.dto.CategoryListResDto;
import com.sparta.oneeat.category.dto.CreateCategoryReqDto;
import com.sparta.oneeat.category.dto.CreateCategoryResDto;
import com.sparta.oneeat.category.dto.UpdateCategoryReqDto;
import com.sparta.oneeat.user.entity.User;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface StoreCategoryService {
    CreateCategoryResDto createCategory(Long id, CreateCategoryReqDto createCategoryReqDto);

    void updateCategory(User user, UpdateCategoryReqDto createCategoryReqDto, UUID categoryId);
  
    void deleteCategory(User user, UUID categoryId);

    void hideCategory(User user, UUID categoryId);

    Page<CategoryListResDto> getStoreCategoryList(Long id, int i, int size, String sort, boolean isAsc);
}
