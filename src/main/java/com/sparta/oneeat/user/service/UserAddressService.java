package com.sparta.oneeat.user.service;

import com.sparta.oneeat.auth.service.UserDetailsImpl;

import java.util.UUID;

public interface UserAddressService {

    UUID creatAddress(UserDetailsImpl userDetails, String address);
}
