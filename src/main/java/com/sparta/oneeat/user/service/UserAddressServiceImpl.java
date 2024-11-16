package com.sparta.oneeat.user.service;

import com.sparta.oneeat.common.exception.CustomException;
import com.sparta.oneeat.common.exception.ExceptionType;
import com.sparta.oneeat.user.dto.AddressResponseDto;
import com.sparta.oneeat.user.entity.User;
import com.sparta.oneeat.user.entity.UserAddress;
import com.sparta.oneeat.user.repository.UserAddressRepository;
import com.sparta.oneeat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl implements UserAddressService {

    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;

    @Override
    @Transactional
    public UUID creatAddress(User user, String address) {

        // 회원 아이디 가져와서 유저 검증
        userRepository.findById(user.getId()).orElseThrow(()->
                new CustomException(ExceptionType.USER_NOT_EXIST)
        );

        // 이미 같은 주소가 등록되어있는지 확인
        if(userAddressRepository.findByUserIdAndAddress(user.getId(), address).isPresent()){
            throw new CustomException(ExceptionType.USER_EXIST_ADDRESS);
        }


        UserAddress userAddress = new UserAddress(user, address);
        userAddressRepository.save(userAddress);

        return userAddress.getId();
    }

    @Override
    public List<AddressResponseDto> selectAddressList(Long userId) {
        // FK로 사용되는 유저가 존재하는지
        userRepository.findById(userId).orElseThrow(()->
                new CustomException(ExceptionType.USER_NOT_EXIST)
        );

        List<UserAddress> userAddressList =
                userAddressRepository.findByUserId(userId);

        // 등록된 주소가 없다면 예외처리
        if (userAddressList.isEmpty()) {
            throw new CustomException(ExceptionType.USER_NOT_EXIST_ADDRESS);
        }

        return userAddressList.stream()
                .map(address -> new AddressResponseDto(address.getId(), address.getAddress()))
                .toList();
    }

    @Override
    @Transactional
    public void modifyCurrentAddress(User user, UUID addressId) {
        // 자신의 주소록에 주소가 있는지 확인
        UserAddress userAddress = userAddressRepository.findByIdAndUserId(addressId, user.getId()).orElseThrow(()->
                new CustomException(ExceptionType.USER_NOT_EXIST_ADDRESS)
        );

        // 유저의 기본 주소에 반영하기
        user.modifyCurrentAddress(userAddress.getAddress());

    }

    @Override
    @Transactional
    public void modifyAddress(Long userId, UUID addressId, String address) {
        // 자신의 주소록에 주소가 있는지 확인
        UserAddress userAddress = userAddressRepository.findByIdAndUserId(addressId, userId).orElseThrow(()->
                new CustomException(ExceptionType.USER_NOT_EXIST_ADDRESS)
        );

        // 주소 수정
        userAddress.modifyAddress(address);
    }

    @Override
    @Transactional
    public void softDeleteAddress(Long userId, UUID addressId) {
        // 자신의 주소록에 주소가 있는지 확인
        UserAddress userAddress = userAddressRepository.findByIdAndUserId(addressId, userId).orElseThrow(()->
                new CustomException(ExceptionType.USER_NOT_EXIST_ADDRESS)
        );

        // 숨김처리
        userAddress.softDelete(userId);
    }

    @Override
    @Transactional
    public void hardDeleteAddress(UUID addressId) {
        // 전체 회원 주소록에 주소가 있는지 확인
        UserAddress userAddress = userAddressRepository.findById(addressId).orElseThrow(()->
                new CustomException(ExceptionType.USER_NOT_EXIST_ADDRESS)
        );
        // 숨김처리 상태인지 확인
        if(userAddress.getDeletedAt() == null){
            throw new CustomException(ExceptionType.USER_NOT_SOFT_DELETE_ADDRESS);
        }

        // 삭제 처리
        userAddressRepository.deleteById(addressId);
    }
}
