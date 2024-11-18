package com.sparta.oneeat.store.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateStoreResDto {

    private UUID storeId;

    public CreateStoreResDto(UUID storeId) {
        this.storeId = storeId;
    }
}
