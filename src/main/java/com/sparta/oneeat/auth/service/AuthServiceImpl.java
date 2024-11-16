package com.sparta.oneeat.auth.service;

import com.sparta.oneeat.auth.dto.SignupRequestDto;
import com.sparta.oneeat.auth.dto.SignupResponseDto;
import com.sparta.oneeat.common.exception.CustomException;
import com.sparta.oneeat.common.exception.ExceptionType;
import com.sparta.oneeat.user.entity.User;
import com.sparta.oneeat.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public SignupResponseDto signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        String nickname = requestDto.getNickname();

        password = passwordEncoder.encode(password);
        log.info("비밀번호를 암호화 했습니다: {}", password);

        if (userRepository.findByName(username).isPresent()) {
            log.warn("중복된 ID 입니다: {}", username);
            throw new CustomException(ExceptionType.AUTH_DUPLICATE_USERNAME);
        }

        if (userRepository.findByNickname(username).isPresent()) {
            log.warn("이미 존재하는 닉네임 입니다: {}", nickname);
            throw new CustomException(ExceptionType.AUTH_DUPLICATE_NICKNAME);
        }

        User user = new User(username, password, nickname, requestDto.getEmail(), requestDto.getAddress());

        userRepository.save(user);
        log.info("회원가입이 성공적으로 되었습니다. ID: {}", user.getName());

        return new SignupResponseDto(user.getId());
    }

}

