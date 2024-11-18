package com.sparta.oneeat.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.UUID;

@Getter
public class AddressModifyRequestDto {
    @NotBlank
    UUID addressId;
}
