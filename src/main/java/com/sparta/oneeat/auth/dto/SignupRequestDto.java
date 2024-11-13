package com.sparta.oneeat.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String address;
}