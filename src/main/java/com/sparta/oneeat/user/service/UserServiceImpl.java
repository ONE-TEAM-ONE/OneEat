package com.sparta.oneeat.user.service;

import com.sparta.oneeat.auth.service.UserDetailsImpl;
import com.sparta.oneeat.common.exception.CustomException;
import com.sparta.oneeat.common.exception.ExceptionType;
import com.sparta.oneeat.user.dto.EmailRequestDto;
import com.sparta.oneeat.user.dto.NicknameRequestDto;
import com.sparta.oneeat.user.dto.PasswordRequestDto;
import com.sparta.oneeat.user.dto.UserResponseDto;
import com.sparta.oneeat.user.entity.User;
import com.sparta.oneeat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    @Transactional
    public void softDeleteUser(UserDetailsImpl userDetails, String password) {
        // 암호화된 비밀번호 비교
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new CustomException(ExceptionType.USER_PASSWORD_MISMATCH);
        }

        User user = userRepository.findById(userDetails.getId()).orElseThrow(() ->
               new CustomException(ExceptionType.USER_NOT_EXIST)
        );

        user.softDelete(userDetails.getId());

    }

    @Override
    @Transactional
    public void hardDeleteUser(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ExceptionType.USER_NOT_EXIST)
        );

        // 숨김 처리가 되었는지 확인
        if(user.getDeletedAt() == null)
            throw new CustomException(ExceptionType.USER_NOT_SOFT_DELETE);

        userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public void modifyPassword(UserDetailsImpl userDetails, PasswordRequestDto passwordRequestDto) {

        User user = userRepository.findById(userDetails.getId()).orElseThrow(()->
                new CustomException(ExceptionType.USER_NOT_EXIST)
        );

        if(passwordEncoder.matches(passwordRequestDto.getOldPassword(), user.getPassword())){
            throw new CustomException(ExceptionType.USER_PASSWORD_MISMATCH);
        }

        user.modifyPassword(passwordRequestDto.getNewPassword());

    }

    @Override
    @Transactional
    public void modifyNickname(UserDetailsImpl userDetails, NicknameRequestDto nicknameRequestDto) {

        User user = userRepository.findById(userDetails.getId()).orElseThrow(()->
                new CustomException(ExceptionType.USER_NOT_EXIST)
        );

        // 닉네임 중복 확인
        if(userRepository.findByNickname(nicknameRequestDto.getNickname()).isPresent())
            throw new CustomException(ExceptionType.USER_EXIST_NICKNAME);

        user.modifyNickname(nicknameRequestDto.getNickname());

    }

    @Override
    @Transactional
    public void modifyEmail(UserDetailsImpl userDetails, EmailRequestDto emailRequestDto) {

        User user = userRepository.findById(userDetails.getId()).orElseThrow(()->
                new CustomException(ExceptionType.USER_NOT_EXIST)
        );

        // 이메일 중복 확인
        if(userRepository.findByEmail(emailRequestDto.getEmail()).isPresent())
            throw new CustomException(ExceptionType.USER_EXIST_EMAIL);

        user.modifyEmail(emailRequestDto.getEmail());

    }

}
