package com.sparta.oneeat.user.service;

import com.sparta.oneeat.user.dto.UserResponseDto;

public interface UserService {
    UserResponseDto selectUserDetails(Long id);
}
