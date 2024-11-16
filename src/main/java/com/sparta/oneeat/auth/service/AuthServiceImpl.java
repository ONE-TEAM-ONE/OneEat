package com.sparta.oneeat.auth.service;

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
    public SignupResponseDto signup(String username, String password, String nickname, String email, String address) {

        password = passwordEncoder.encode(password);
        log.info("비밀번호를 암호화 했습니다: {}", password);

        if (userRepository.findByName(username).isPresent()) {
            log.warn("중복된 유저 네임 입니다: {}", username);
            throw new CustomException(ExceptionType.AUTH_DUPLICATE_USERNAME);
        }

        if (userRepository.findByNickname(username).isPresent()) {
            log.warn("중복된 닉네임 입니다: {}", nickname);
            throw new CustomException(ExceptionType.AUTH_DUPLICATE_NICKNAME);
        }

        User user = new User(username, password, nickname, email, address);

        userRepository.save(user);
        log.info("회원가입이 성공적으로 되었습니다. Username: {}", user.getName());

        return new SignupResponseDto(user.getId());
    }

}

