package com.sparta.oneeat.store.service;

import com.sparta.oneeat.store.dto.StoreListDto;
import org.springframework.data.domain.Page;

public interface StoreService {

    // 가게 리스트 조회
    Page<StoreListDto> getStoreList(long userId, int page, int size, String sort, boolean isAsc);

}
