package com.sparta.oneeat.auth.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
    private String username;
    private String password;
}