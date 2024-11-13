package com.sparta.oneeat.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignupResponseDto {
    Long userId;
}
