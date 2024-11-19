package com.sparta.oneeat.auth.service;

import com.sparta.oneeat.auth.dto.SignupResponseDto;

public interface AuthService {
    SignupResponseDto signup(String username, String password, String nickname, String email, String address);
}
