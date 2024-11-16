package com.sparta.oneeat.store.dto;

import com.sparta.oneeat.menu.dto.response.MenuResponseDto;
import com.sparta.oneeat.review.dto.ReviewListDto;
import com.sparta.oneeat.store.entity.Store;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class StoreDetailDto {
    private UUID storeId;
    private String name;
    private String address;
    private String desc;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
    private String owner;
    private Integer minPrice;
    private String category;
    private List<String> deliveryRegions;
    private List<MenuResponseDto> menus;
    private List<ReviewListDto> reviews;

    public StoreDetailDto(Store store, List<String> deliveryRegions, List<MenuResponseDto> menus, List<ReviewListDto> reviews) {
        this.storeId = store.getId();
        this.name = store.getName();
        this.address = store.getAddress();
        this.desc = store.getDescription();
        this.startTime = store.getStartTime();
        this.endTime = store.getEndTime();
        this.status = store.getStatus().name();
        this.owner = store.getOwner();
        this.minPrice = store.getMinPrice();
        this.category = store.getCategory().getCategoryName();
        this.deliveryRegions = deliveryRegions;
        this.menus = menus;
        this.reviews = reviews;
    }

}