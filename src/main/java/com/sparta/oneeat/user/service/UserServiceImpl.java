package com.sparta.oneeat.user.service;

import com.sparta.oneeat.common.exception.CustomException;
import com.sparta.oneeat.common.exception.ExceptionType;
import com.sparta.oneeat.user.dto.UserResponseDto;
import com.sparta.oneeat.user.entity.User;
import com.sparta.oneeat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDto selectUserDetails(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new CustomException(ExceptionType.ACCESS_DENIED)
        );

        log.info("username" + user.getName());

        return new UserResponseDto(
                user.getName(),
                user.getNickname(),
                user.getEmail(),
                user.getCurrentAddress(),
                user.getRole()
        );
    }
}
