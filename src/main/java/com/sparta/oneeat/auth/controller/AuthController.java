package com.sparta.oneeat.auth.controller;

import com.sparta.oneeat.auth.dto.SignupRequestDto;
import com.sparta.oneeat.auth.service.AuthService;
import com.sparta.oneeat.common.response.BaseResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "회원가입 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "409", description = "존재하는 식별 값"),
            @ApiResponse(responseCode = "500", description = "회원가입 실패")
    })
    @PostMapping("/signup")
    public ResponseEntity<? extends BaseResponseBody> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return ResponseEntity
                .status(200)
                .body(BaseResponseBody.of(
                        0, authService.signup(signupRequestDto).getUserId()
                ));
    }

}
