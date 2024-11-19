
package com.sparta.oneeat.user.service;

import com.sparta.oneeat.user.dto.UserResponseDto;

public interface UserService {
    UserResponseDto selectUserDetails(Long id);

    void softDeleteUser(Long userId, String password);

    void hardDeleteUser(Long userId);

    void modifyPassword(Long userId, String oldPassword, String newPassword);

    void modifyNickname(Long userId, String nickname);

    void modifyEmail(Long userId, String email);
}