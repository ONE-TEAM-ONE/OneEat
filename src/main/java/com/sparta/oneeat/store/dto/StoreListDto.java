package com.sparta.oneeat.store.dto;

import com.sparta.oneeat.store.entity.Store;
import com.sparta.oneeat.store.entity.StoreStatusEnum;
import lombok.Getter;

import java.util.UUID;

@Getter
public class StoreListDto {
    private UUID storeId;
    private String storeName;
    private int minPrice;
    private StoreStatusEnum status;
    private String category;

    public StoreListDto(Store store){
        this.storeId = store.getId();
        this.storeName = store.getName();
        this.status = store.getStatus();
        this.minPrice = store.getMinPrice();
        this.category = store.getCategory().getCategoryName();
    }
}
