package com.sparta.oneeat.store.service;

import com.sparta.oneeat.common.exception.CustomException;
import com.sparta.oneeat.common.exception.ExceptionType;
import com.sparta.oneeat.store.dto.request.CategoryRequestDto;
import com.sparta.oneeat.store.entity.Category;
import com.sparta.oneeat.store.repository.CategoryRepository;
import com.sparta.oneeat.user.entity.User;
import com.sparta.oneeat.user.entity.UserRoleEnum;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    // 카테고리 수정
    public void updateCategory(User user, CategoryRequestDto requestDto, UUID categoryId) {
        Category category;
        if (user.getRole() == UserRoleEnum.CUSTOMER || user.getRole() == UserRoleEnum.OWNER) {
            log.info("권한 접근 제한 : {}", user.getRole());
            throw new CustomException(ExceptionType.INTERNAL_SERVER_ERROR); //Todo 유저 관련 타입으로 수정 필요
        }

        //Todo 수정할 카테고리 존재 여부 검증
        log.info("카테고리 검증 : ");
        category = categoryRepository.findById(categoryId).orElseThrow(() -> new CustomException(
            ExceptionType.INTERNAL_SERVER_ERROR)); //Todo 카테고리 관련 타입으로 수정 필요함

        //Todo 카테고리 수정 -> 이미 존재하는 카테고리면 예외
        if (categoryRepository.findByCategoryName(requestDto.getCategoryName()).isPresent()) {
            log.info("이미 존재하는 카테고리 : ");
            throw new CustomException(ExceptionType.INTERNAL_SERVER_ERROR); //Todo 이미 존재하는 카테고리 타입 수정 필요
        }

        //Todo 카테고리 수정 처리
        category.updateCategoryName(requestDto);
    }

}
