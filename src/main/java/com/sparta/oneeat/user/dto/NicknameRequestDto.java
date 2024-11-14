package com.sparta.oneeat.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class NicknameRequestDto {
    @NotBlank
    private String nickname;
}
