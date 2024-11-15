package com.sparta.oneeat.store.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateStoreResDto {

    private UUID storeId;
    private List<String> deliveryRegions;

    public CreateStoreResDto(UUID storeId, List<String> deliveryRegionStrings) {
        this.storeId = storeId;
        this.deliveryRegions = deliveryRegionStrings;
    }
}
