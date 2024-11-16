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

        this.validateUserExist(user.getId());

        if (userAddressRepository.findByUserIdAndAddress(user.getId(), address).isPresent()) {
            log.warn("이미 같은 주소가 회원의 주소록에 존재합니다.");
            throw new CustomException(ExceptionType.USER_EXIST_ADDRESS);
        }

        UserAddress userAddress = new UserAddress(user, address);
        userAddressRepository.save(userAddress);
        log.info("주소가 추가되었습니다. Address: {}", userAddress.getAddress());

        return userAddress.getId();
    }

    @Override
    public List<AddressResponseDto> selectAddressList(Long userId) {

        this.validateUserExist(userId);

        List<UserAddress> userAddressList = userAddressRepository.findByUserId(userId);
        if (userAddressList.isEmpty()) {
            log.warn("회원의 주소록에 주소가 존재하지 않습니다.");
            throw new CustomException(ExceptionType.USER_NOT_EXIST_ADDRESS);
        }

        return userAddressList.stream()
                .map(address -> new AddressResponseDto(address.getId(), address.getAddress()))
                .toList();
    }

    @Override
    @Transactional
    public void modifyCurrentAddress(User user, UUID addressId) {

        this.validateUserExist(user.getId());

        UserAddress userAddress = this.validateAddressExist(user.getId(), addressId);

        user.modifyCurrentAddress(userAddress.getAddress());
        log.info("회원의 현재 주소가 변경되었습니다. CurrentAddress: {}", user.getCurrentAddress());
    }

    @Override
    @Transactional
    public void modifyAddress(Long userId, UUID addressId, String address) {

        this.validateUserExist(userId);

        UserAddress userAddress = this.validateAddressExist(userId, addressId);

        userAddress.modifyAddress(address);
        log.info("주소 내용이 변경되었습니다. Address: {}", userAddress.getAddress());
    }

    @Override
    @Transactional
    public void softDeleteAddress(Long userId, UUID addressId) {

        this.validateUserExist(userId);

        UserAddress userAddress = validateAddressExist(userId, addressId);

        userAddress.softDelete(userId);
        log.info("주소가 비활성화 되었습니다. DeletedAt: {}", userAddress.getDeletedAt());
    }

    @Override
    @Transactional
    public void hardDeleteAddress(UUID addressId) {

        UserAddress userAddress = userAddressRepository.findById(addressId).orElseThrow(() ->
                new CustomException(ExceptionType.USER_NOT_EXIST_ADDRESS)
        );
        log.info("주소 정보가 확인되었습니다. Address: {}", userAddress.getAddress());


        if (userAddress.getDeletedAt() == null) {
            log.warn("주소가 비활성화 상태가 아닙니다.");
            throw new CustomException(ExceptionType.USER_NOT_SOFT_DELETE_ADDRESS);
        }

        userAddressRepository.deleteById(addressId);
        log.info("주소가 삭제되었습니다. DeletedAt: {}", userAddress.getDeletedAt());
    }

    protected User validateUserExist(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ExceptionType.USER_NOT_EXIST)
        );
        log.info("회원 정보가 확인되었습니다. userId: {}", userId);

        return user;
    }

    protected UserAddress validateAddressExist(Long userId, UUID addressId) {
        UserAddress userAddress = userAddressRepository.findByIdAndUserId(addressId, userId).orElseThrow(() ->
                new CustomException(ExceptionType.USER_NOT_EXIST_ADDRESS)
        );
        log.info("회원의 주소록에 주소가 존재합니다. Address: {}", userAddress.getAddress());

        return userAddress;
    }
}
