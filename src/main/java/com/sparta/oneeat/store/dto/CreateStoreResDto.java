package com.sparta.oneeat.store.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateStoreResDto {

    private UUID storeId;

    public CreateStoreResDto(UUID storeId) {
        this.storeId = storeId;
    }
}
