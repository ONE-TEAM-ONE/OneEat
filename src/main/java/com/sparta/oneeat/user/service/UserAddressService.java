package com.sparta.oneeat.user.service;

import com.sparta.oneeat.auth.service.UserDetailsImpl;
import com.sparta.oneeat.user.dto.AddressResponseDto;

import java.util.List;
import java.util.UUID;

public interface UserAddressService {

    UUID creatAddress(UserDetailsImpl userDetails, String address);

    List<AddressResponseDto> selectAddressList(UserDetailsImpl userDetails);

    void modifyCurrentAddress(UserDetailsImpl userDetails, UUID addressId);

    void modifyAddress(UserDetailsImpl userDetails, UUID addressId, String address);

    void softDeleteAddress(UserDetailsImpl userDetails, UUID addressId);

    void hardDeleteAddress(UserDetailsImpl userDetails, UUID addressId);
}
