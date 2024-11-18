package com.sparta.oneeat.store.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class UpdateStoreResDto {
    private UUID storeId;

    public UpdateStoreResDto(UUID storeId) {
        this.storeId = storeId;
    }
}