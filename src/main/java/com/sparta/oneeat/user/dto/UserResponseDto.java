package com.sparta.oneeat.user.dto;

import com.sparta.oneeat.user.entity.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private String username;
    private String nickname;
    private String email;
    private String address;
    private UserRoleEnum role;
}
