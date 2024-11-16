package com.sparta.oneeat.user.service;

import com.sparta.oneeat.common.exception.CustomException;
import com.sparta.oneeat.common.exception.ExceptionType;
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
                new CustomException(ExceptionType.USER_NOT_EXIST)
        );
        log.info("회원의 정보가 확인되었습니다. ID: {}", id);

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
    public void softDeleteUser(Long userId, String password, String receivedPassword) {

        if(passwordEncoder.matches(receivedPassword, password)){
            log.warn("회원의 비밀번호가 일치하지 않습니다.");
            throw new CustomException(ExceptionType.USER_PASSWORD_MISMATCH);
        }

        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ExceptionType.USER_NOT_EXIST)
        );
        log.info("회원의 정보가 확인되었습니다. ID: {}", userId);

        user.softDelete(userId);
        log.info("회원이 비활성화 되었습니다. DeletedAt: {}", user.getDeletedAt());

    }

    @Override
    @Transactional
    public void hardDeleteUser(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ExceptionType.USER_NOT_EXIST)
        );
        log.info("삭제하려는 회원의 정보가 확인되었습니다. ID: {}", userId);

        if(user.getDeletedAt() == null){
            log.warn("삭제하려는 회원이 비활성화 상태가 아닙니다.");
            throw new CustomException(ExceptionType.USER_NOT_SOFT_DELETE);
        }

        userRepository.deleteById(userId);
        log.info("회원이 삭제되었습니다. DeletedBy: {}", user.getDeletedBy());
    }

    @Override
    @Transactional
    public void modifyPassword(Long userId, String oldPassword, String newPassword) {

        User user = userRepository.findById(userId).orElseThrow(()->
                new CustomException(ExceptionType.USER_NOT_EXIST)
        );
        log.info("회원의 정보가 확인되었습니다. ID: {}", userId);

        if(!passwordEncoder.matches(oldPassword, user.getPassword())){
            log.warn("회원의 비밀번호가 일치하지 않습니다.");
            throw new CustomException(ExceptionType.USER_PASSWORD_MISMATCH);
        }

        user.modifyPassword(passwordEncoder.encode(newPassword));
        log.info("회원의 비밀번호가 암호화되어 변경되었습니다.");

    }

    @Override
    @Transactional
    public void modifyNickname(Long userId, String nickname) {

        User user = userRepository.findById(userId).orElseThrow(()->
                new CustomException(ExceptionType.USER_NOT_EXIST)
        );
        log.info("회원의 정보가 확인되었습니다. ID: {}", userId);

        user.modifyNickname(nickname);
        log.info("회원의 닉네임이 변경되었습니다. nickname: {}", user.getNickname());

    }

    @Override
    @Transactional
    public void modifyEmail(Long userId, String email) {

        User user = userRepository.findById(userId).orElseThrow(()->
                new CustomException(ExceptionType.USER_NOT_EXIST)
        );
        log.info("회원의 정보가 확인되었습니다. ID: {}", userId);

        if(userRepository.findByEmail(email).isPresent()){
            log.warn("이미 존재하는 이메일 입니다. Email: {}", email);
            throw new CustomException(ExceptionType.USER_EXIST_EMAIL);
        }

        user.modifyEmail(email);
        log.info("회원의 이메일이 변경되었습니다. Email: {}", user.getEmail());

    }

}