package com.sparta.oneeat.user.service;

import com.sparta.oneeat.auth.service.UserDetailsImpl;
import com.sparta.oneeat.user.dto.NicknameRequestDto;
import com.sparta.oneeat.user.dto.PasswordRequestDto;
import com.sparta.oneeat.user.dto.UserResponseDto;

public interface UserService {
    UserResponseDto selectUserDetails(Long id);

    void softDeleteUser(UserDetailsImpl userDetails, String password);

    void hardDeleteUser(Long userId);

    void modifyPassword(UserDetailsImpl userDetails, PasswordRequestDto passwordRequestDto);

    void modifyNickname(UserDetailsImpl userDetails, NicknameRequestDto nicknameRequestDto);
}
