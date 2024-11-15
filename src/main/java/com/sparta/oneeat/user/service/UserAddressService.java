package com.sparta.oneeat.user.service;

import com.sparta.oneeat.auth.service.UserDetailsImpl;
import com.sparta.oneeat.user.dto.AddressResponseDto;

import java.util.List;
import java.util.UUID;

public interface UserAddressService {

    UUID creatAddress(UserDetailsImpl userDetails, String address);

    List<AddressResponseDto> selectAddressList(UserDetailsImpl userDetails);
}
