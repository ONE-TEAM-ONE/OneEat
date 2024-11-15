package com.sparta.oneeat.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class AddressResponseDto {
    private UUID addressId;
    private String address;
}
