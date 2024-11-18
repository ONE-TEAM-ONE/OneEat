package com.sparta.oneeat.user.service;

import com.sparta.oneeat.user.dto.AddressResponseDto;
import com.sparta.oneeat.user.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserAddressService {

    UUID creatAddress(User user, String address);

    List<AddressResponseDto> selectAddressList(Long userId);

    void modifyCurrentAddress(User user, UUID addressId);

    void modifyAddress(Long userId, UUID addressId, String address);

    void softDeleteAddress(Long userId, UUID addressId);

    void hardDeleteAddress(UUID addressId);
}
