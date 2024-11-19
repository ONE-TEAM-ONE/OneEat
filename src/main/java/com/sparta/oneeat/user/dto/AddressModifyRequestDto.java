package com.sparta.oneeat.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class AddressModifyRequestDto {
    @NotNull
    UUID addressId;
}
