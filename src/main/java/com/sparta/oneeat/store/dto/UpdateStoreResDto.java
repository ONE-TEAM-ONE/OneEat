package com.sparta.oneeat.store.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class UpdateStoreResDto {
    private UUID storeId;
    private String name;

    public UpdateStoreResDto(UUID storeId, String name) {
        this.storeId = storeId;
        this.name = name;
    }
}