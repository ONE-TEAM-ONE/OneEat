package com.sparta.oneeat.auth.service;

import com.sparta.oneeat.auth.dto.SignupRequestDto;
import com.sparta.oneeat.auth.dto.SignupResponseDto;
import com.sparta.oneeat.common.exception.CustomException;
import com.sparta.oneeat.common.exception.ExceptionType;
import com.sparta.oneeat.user.entity.User;
import com.sparta.oneeat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SignupResponseDto signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        String nickname = requestDto.getNickname();

        if (password == null || password.isEmpty())
            throw new CustomException(ExceptionType.AUTH_INVALID_PASSWORD);

        password = passwordEncoder.encode(password);

        // 유저네임(아이디) 중복 체크
        if(userRepository.findByName(username).isPresent())
            throw new CustomException(ExceptionType.AUTH_DUPLICATE_USERNAME);

        // 닉네임 중복 체크
        if (userRepository.findByNickname(username).isPresent())
            throw new CustomException(ExceptionType.AUTH_DUPLICATE_NICKNAME);

        User user = User.builder()
                .name(username)
                .password(password)
                .nickname(nickname)
                .email(requestDto.getEmail())
                .currentAddress(requestDto.getAddress())
                .build();

        userRepository.save(user);

        return SignupResponseDto.builder()
                .userId(user.getId())
                .build();
    }
}

