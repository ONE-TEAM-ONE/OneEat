package com.sparta.oneeat.user.service;

import com.sparta.oneeat.auth.service.UserDetailsImpl;
import com.sparta.oneeat.common.exception.CustomException;
import com.sparta.oneeat.common.exception.ExceptionType;
import com.sparta.oneeat.user.entity.UserAddress;
import com.sparta.oneeat.user.repository.UserAddressRepository;
import com.sparta.oneeat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl implements UserAddressService {

    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;

    @Override
    @Transactional
    public UUID creatAddress(UserDetailsImpl userDetails, String address) {

        // 회원 아이디 가져와서 유저 검증
        userRepository.findById(userDetails.getId()).orElseThrow(()->
                new CustomException(ExceptionType.USER_NOT_EXIST)
        );

        // 이미 같은 주소가 등록되어있는지 확인
        if(userAddressRepository.findByUserIdAndAddress(userDetails.getId(), address).isPresent()){
            throw new CustomException(ExceptionType.USER_EXIST_ADDRESS);
        }


        UserAddress userAddress = new UserAddress(userDetails.getUser(), address);
        userAddressRepository.save(userAddress);

        return userAddress.getId();
    }
}
