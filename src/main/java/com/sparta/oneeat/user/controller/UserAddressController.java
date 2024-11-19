package com.sparta.oneeat.user.controller;

import com.sparta.oneeat.auth.service.UserDetailsImpl;
import com.sparta.oneeat.common.response.BaseResponseBody;
import com.sparta.oneeat.user.dto.AddressModifyRequestDto;
import com.sparta.oneeat.user.dto.AddressRequestDto;
import com.sparta.oneeat.user.service.UserAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/user/address")
@RequiredArgsConstructor
@Tag(name="User Address", description="User Address API")
public class UserAddressController {

    private final UserAddressService userAddressService;

    @Operation(summary = "주소 추가", description = "자신의 주소록에 주소를 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주소 추가 성공"),
            @ApiResponse(responseCode = "500", description = "주소 추가 실패")
    })
    @PostMapping
    public ResponseEntity<? extends BaseResponseBody> creatAddress(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Validated @RequestBody AddressRequestDto addressRequestDto
    ){
        return ResponseEntity.status(200).body(BaseResponseBody.of(0,
                userAddressService.creatAddress(userDetails.getUser(), addressRequestDto.getAddress())
        ));
    }

    @Operation(summary = "주소 목록 조회", description = "자신의 주소록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "목록 조회 성공"),
            @ApiResponse(responseCode = "500", description = "목록 조회 실패")
    })
    @GetMapping
    public ResponseEntity<? extends BaseResponseBody> selcectAddressList(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        return ResponseEntity.status(200).body(BaseResponseBody.of(0,
                userAddressService.selectAddressList(userDetails.getId())
        ));
    }

    @Operation(summary = "기본 주소 설정", description = "자신의 기본 주소를 설정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주소 설정 성공"),
            @ApiResponse(responseCode = "500", description = "주소 설정 실패")
    })
    @PatchMapping
    public ResponseEntity<? extends BaseResponseBody> modifyCurrentAddress(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Validated @RequestBody AddressModifyRequestDto addressModifyRequestDto
    ){
        userAddressService.modifyCurrentAddress(userDetails.getUser(), addressModifyRequestDto.getAddressId());
        return ResponseEntity.status(200).body(BaseResponseBody.of(0, null));
    }

    @Operation(summary = "주소록 주소 수정", description = "자신의 주소록의 주소를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주소 수정 성공"),
            @ApiResponse(responseCode = "500", description = "주소 수정 실패")
    })
    @PutMapping(value = "/{addressId}")
    public ResponseEntity<? extends BaseResponseBody> modifyAddress(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID addressId,
            @Validated @RequestBody AddressRequestDto addressRequestDto
    ){
        userAddressService.modifyAddress(userDetails.getId(), addressId, addressRequestDto.getAddress());
        return ResponseEntity.status(200).body(BaseResponseBody.of(0, null));
    }

    @Operation(summary = "주소록 주소 숨김", description = "자신의 주소록의 주소를 숨김처리 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주소 숨김 성공"),
            @ApiResponse(responseCode = "500", description = "주소 숨김 실패")
    })
    @PatchMapping(value = "/{addressId}")
    public ResponseEntity<? extends BaseResponseBody> softDeleteAddress(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID addressId
    ){
        userAddressService.softDeleteAddress(userDetails.getId(), addressId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(0, null));
    }

    @Operation(summary = "주소록 주소 삭제", description = "회원 주소록의 주소를 삭제처리 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주소 삭제 성공"),
            @ApiResponse(responseCode = "500", description = "주소 삭제 실패")
    })
    @DeleteMapping(value = "/{addressId}")
    public ResponseEntity<? extends BaseResponseBody> hardDeleteAddress(
            @PathVariable UUID addressId
    ){
        userAddressService.hardDeleteAddress(addressId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(0, null));
    }
}
