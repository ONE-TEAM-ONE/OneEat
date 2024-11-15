package com.sparta.oneeat.store.dto;

import com.sparta.oneeat.store.entity.Store;
import com.sparta.oneeat.store.entity.StoreCategoryEnum;
import com.sparta.oneeat.store.entity.StoreStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class StoreDetailDto {
    private UUID storeId;
    private String storeName;
    private int minPrice;
    private StoreStatusEnum status;
    private StoreCategoryEnum category;
    private String address;
    private String desc;
    private Date startTime;
    private Date endTime;
    private String owner;

    // 메뉴 목록
//    private List<MenuResponseDto> menuList = new ArrayList<>();

    public StoreDetailDto(Store store) {
        this.storeId = store.getId();
        this.storeName = store.getName();
        this.status = store.getStatus();
        this.minPrice = store.getMinPrice();
        this.category = store.getCategory().getCategory();
        this.address = store.getAddress();
        this.desc = store.getDescription();
        this.startTime = store.getStartTime();
        this.endTime = store.getEndTime();
        this.owner = store.getOwner();
    }
}
