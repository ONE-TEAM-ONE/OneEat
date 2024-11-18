package com.sparta.oneeat.category.service;

import com.sparta.oneeat.category.dto.CategoryListResDto;
import com.sparta.oneeat.category.dto.CreateCategoryReqDto;
import com.sparta.oneeat.category.dto.CreateCategoryResDto;
import com.sparta.oneeat.category.dto.UpdateCategoryReqDto;
import com.sparta.oneeat.category.entity.Category;
import com.sparta.oneeat.category.repository.StoreCategotyRepository;
import com.sparta.oneeat.common.exception.CustomException;
import com.sparta.oneeat.common.exception.ExceptionType;
import com.sparta.oneeat.store.repository.StoreRepository;
import com.sparta.oneeat.user.entity.User;
import com.sparta.oneeat.user.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreCategoryServiceImpl implements StoreCategoryService{

    private final UserRepository userRepository;
    private final StoreCategotyRepository storeCategotyRepository;
    private final StoreRepository storeRepository;

    @Override
    @Transactional
    public CreateCategoryResDto createCategory(Long userId, CreateCategoryReqDto createCategoryReqDto) {

        // 중복 확인
        if(storeCategotyRepository.findByCategoryName(createCategoryReqDto.getCategory()).isPresent()) throw new CustomException(ExceptionType.CATEGORY_DUPLICATED);

        // 카테고리 생성
        Category saved = storeCategotyRepository.save(new Category(createCategoryReqDto));

        return new CreateCategoryResDto(saved.getId());
    }
  
    @Override
    @Transactional
    public void updateCategory(User user, UpdateCategoryReqDto updateCategoryReqDto, UUID categoryId) {

        //Todo 수정할 카테고리 존재 여부 검증
        log.info("카테고리 검증 : ");
        Category category = storeCategotyRepository.findById(categoryId).orElseThrow(() -> new CustomException(
            ExceptionType.INTERNAL_SERVER_ERROR)); //Todo 카테고리 관련 타입으로 수정 필요함
        //Todo 카테고리 수정 -> 이미 존재하는 카테고리면 예외
        if (storeCategotyRepository.findByCategoryName(updateCategoryReqDto.getCategoryName()).isPresent()) {
            log.info("이미 존재하는 카테고리 : ");
            throw new CustomException(ExceptionType.INTERNAL_SERVER_ERROR); //Todo 이미 존재하는 카테고리 타입 수정 필요
        }
        //Todo 카테고리 수정 처리
        category.updateCategoryName(updateCategoryReqDto);
    }
  
    @Override
    @Transactional
    public void deleteCategory(User user, UUID categoryId) {

        //Todo 카테고리 존재 여부 검증
        log.info("카테고리 검증 : ");
        Category category = storeCategotyRepository.findById(categoryId).orElseThrow(() -> new CustomException(
            ExceptionType.INTERNAL_SERVER_ERROR)); //Todo 카테고리 관련 타입으로 수정 필요함

        if (!storeRepository.findByCategory(category).isEmpty()) {
            log.info("카테고리 사용 중");
            throw new CustomException(ExceptionType.INTERNAL_SERVER_ERROR); //Todo 카테고리 관련 타입으로 수정 필요함
        }

        //Todo 카테고리 삭제 처리
        storeCategotyRepository.delete(category);
    }

    @Override
    @Transactional
    public void hideCategory(User user, UUID categoryId) {

        //Todo 카테고리 존재 여부 검증
        log.info("카테고리 검증 : ");
        Category category = storeCategotyRepository.findById(categoryId).orElseThrow(() -> new CustomException(
            ExceptionType.INTERNAL_SERVER_ERROR)); //Todo 카테고리 관련 타입으로 수정 필요함

        if (!storeRepository.findByCategory(category).isEmpty()) {
            log.info("카테고리 사용 중");
            throw new CustomException(ExceptionType.INTERNAL_SERVER_ERROR); //Todo 카테고리 관련 타입으로 수정 필요함
        }

        //Todo 카테고리 숨김 처리 -> deleteAt, deleteBy 처리
        category.deleteCategory(user.getId());
    }

    @Override
    public Page<CategoryListResDto> getStoreCategoryList(Long id, int page, int size, String sort, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort1 = Sort.by(direction, sort);
        Pageable pageable = PageRequest.of(page, size, sort1);

        Page<Category> categories = storeCategotyRepository.findAll(pageable);

        return categories.map(CategoryListResDto::new);
    }
}
