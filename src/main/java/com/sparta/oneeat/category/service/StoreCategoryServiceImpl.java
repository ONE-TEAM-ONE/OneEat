package com.sparta.oneeat.category.service;

import com.sparta.oneeat.category.dto.CreateCategoryReqDto;
import com.sparta.oneeat.category.dto.CreateCategoryResDto;
import com.sparta.oneeat.category.entity.Category;
import com.sparta.oneeat.category.repository.StoreCategotyRepository;
import com.sparta.oneeat.common.exception.CustomException;
import com.sparta.oneeat.common.exception.ExceptionType;
import com.sparta.oneeat.user.entity.User;
import com.sparta.oneeat.user.entity.UserRoleEnum;
import com.sparta.oneeat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreCategoryServiceImpl implements StoreCategoryService{

    private final UserRepository userRepository;
    private final StoreCategotyRepository storeCategotyRepository;


    // 관리자 체크 메서드
    private void checkRole(UserRoleEnum role) {
        if(!(role == UserRoleEnum.MANAGER || role == UserRoleEnum.MASTER)) throw new CustomException(ExceptionType.CATEGORY_ACCESS_DENIED);
    }

    @Override
    @Transactional
    public CreateCategoryResDto createCategory(Long userId, CreateCategoryReqDto createCategoryReqDto) {

        // 유저 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));

        // 유저 권한 확인
        checkRole(user.getRole());

        // 중복 확인
        if(storeCategotyRepository.findByCategoryName(createCategoryReqDto.getCategory()).isPresent()) throw new CustomException(ExceptionType.CATEGORY_DUPLICATED);

        // 카테고리 생성
        Category saved = storeCategotyRepository.save(new Category(createCategoryReqDto));

        return new CreateCategoryResDto(saved.getId());
    }
}
