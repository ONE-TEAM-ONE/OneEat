package com.sparta.oneeat.common.controller;

import com.sparta.oneeat.common.service.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/map")
    public ResponseEntity<String> getKakaoApiFromAddress(@RequestParam("address") String address) {
        return addressService.getKakaoApiFromAddress(address);
    }

    @GetMapping("/get-formatted-address")
    public ResponseEntity<String[]> getFormattedAddress(@RequestParam("address") String address) {
        return addressService.getFormattedAddress(address);
    }
}