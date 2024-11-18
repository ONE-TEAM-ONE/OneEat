package com.sparta.oneeat.store.service;

import com.sparta.oneeat.store.dto.CreateStoreReqDto;
import com.sparta.oneeat.store.dto.CreateStoreResDto;
import com.sparta.oneeat.store.dto.UpdateStoreReqDto;
import com.sparta.oneeat.store.dto.UpdateStoreResDto;
import com.sparta.oneeat.store.dto.StoreDetailDto;
import com.sparta.oneeat.store.dto.StoreListDto;
import com.sparta.oneeat.user.entity.User;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface StoreService {

    // 가게 리스트 조회
    Page<StoreListDto> getStoreList(long userId, int page, int size, String sort, boolean isAsc);

    // 가게 생성
    CreateStoreResDto createStore(User user, CreateStoreReqDto createStoreReqDto);

    // 가게 상세 조회
    StoreDetailDto getStoreDetail(UUID storeId,
                                  Integer menuPage, Integer menuSize, String menuSort, Boolean menuIsAsc,
                                  Integer reviewPage, Integer reviewSize, String reviewSort, Boolean reviewIsAsc);

    // 가게 수정
    UpdateStoreResDto updateStore(User user, UUID storeId, UpdateStoreReqDto updateStoreReqDto);

    // 가게 숨김
    void hideStore(long userId, UUID storeId);

}
