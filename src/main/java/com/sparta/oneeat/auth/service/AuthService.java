package com.sparta.oneeat.auth.service;

import com.sparta.oneeat.auth.dto.SignupRequestDto;
import com.sparta.oneeat.auth.dto.SignupResponseDto;

public interface AuthService {
    SignupResponseDto signup(SignupRequestDto signupRequestDto);
}
