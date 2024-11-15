package com.sparta.oneeat.user.controller;

import com.sparta.oneeat.auth.service.UserDetailsImpl;
import com.sparta.oneeat.common.response.BaseResponseBody;
import com.sparta.oneeat.user.service.UserAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
            @RequestBody String address
    ){
        return ResponseEntity.status(200).body(BaseResponseBody.of(0,
                userAddressService.creatAddress(userDetails, address)
        ));
    }

}
