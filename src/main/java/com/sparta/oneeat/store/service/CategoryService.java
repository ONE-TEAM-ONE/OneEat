package com.sparta.oneeat.store.service;

import com.sparta.oneeat.common.exception.CustomException;
import com.sparta.oneeat.common.exception.ExceptionType;
import com.sparta.oneeat.store.entity.Category;
import com.sparta.oneeat.store.repository.CategoryRepository;
import com.sparta.oneeat.store.repository.StoreRepository;
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
    private final StoreRepository storeRepository;

    @Transactional
    public void deleteCategory(User user, UUID categoryId) {
        Category category;
        //Todo 권한 검증
        if (user.getRole() == UserRoleEnum.CUSTOMER || user.getRole() == UserRoleEnum.OWNER) {
            log.info("권한 접근 제한 : {}", user.getRole());
            throw new CustomException(ExceptionType.INTERNAL_SERVER_ERROR); //Todo 유저 관련 타입으로 수정 필요
        }

        //Todo 카테고리 존재 여부 검증
        log.info("카테고리 검증 : ");
        category = categoryRepository.findById(categoryId).orElseThrow(() -> new CustomException(
            ExceptionType.INTERNAL_SERVER_ERROR)); //Todo 카테고리 관련 타입으로 수정 필요함

        if (!storeRepository.findByCategory(category).isEmpty()) {
            log.info("카테고리 사용 중");
            throw new CustomException(ExceptionType.INTERNAL_SERVER_ERROR); //Todo 카테고리 관련 타입으로 수정 필요함
        }

        //Todo 카테고리 삭제 처리
        categoryRepository.delete(category);
    }

}
