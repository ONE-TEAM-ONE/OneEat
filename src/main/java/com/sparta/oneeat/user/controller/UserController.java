
package com.sparta.oneeat.user.controller;

import com.sparta.oneeat.auth.service.UserDetailsImpl;
import com.sparta.oneeat.common.response.BaseResponseBody;
import com.sparta.oneeat.user.dto.EmailRequestDto;
import com.sparta.oneeat.user.dto.NicknameRequestDto;
import com.sparta.oneeat.user.dto.PasswordRequestDto;
import com.sparta.oneeat.user.service.UserService;
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

import java.util.Map;

@Controller
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name="User", description="User API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원 정보 조회", description = "자신의 회원 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 조회 성공"),
            @ApiResponse(responseCode = "500", description = "회원 조회 실패")
    })
    @GetMapping
    public ResponseEntity<? extends BaseResponseBody> selectUserDetails(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        return ResponseEntity.status(200).body(BaseResponseBody.of(0,
                userService.selectUserDetails(userDetails.getId())
        ));
    }

    @Operation(summary = "회원 숨김", description = "회원 정보를 숨김 처리 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 숨김 성공"),
            @ApiResponse(responseCode = "500", description = "회원 숨김 실패")
    })
    @PatchMapping()
    public ResponseEntity<? extends BaseResponseBody> softDeleteUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody Map<String, String> passwordDto
    ){
        userService.softDeleteUser(userDetails.getId(), passwordDto.get("password"));
        return ResponseEntity.status(200).body(BaseResponseBody.of(0, null));
    }

    @Operation(summary = "회원 삭제", description = "회원 정보를 물리적으로 삭제 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 삭제 성공"),
            @ApiResponse(responseCode = "500", description = "회원 삭제 실패")
    })
    @DeleteMapping("/{user_id}")
    public ResponseEntity<? extends BaseResponseBody> hardDeleteUser(
            @PathVariable(name = "user_id") Long userId
    ){
        userService.hardDeleteUser(userId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(0, null));
    }

    @Operation(summary = "비밀번호 변경", description = "자신의 비밀번호를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
            @ApiResponse(responseCode = "500", description = "비밀번호 변경 실패")
    })
    @PutMapping("/password")
    public ResponseEntity<? extends BaseResponseBody> modifyPassword(
            @Validated @RequestBody PasswordRequestDto passwordRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        userService.modifyPassword(userDetails.getId(), passwordRequestDto.getOldPassword(), passwordRequestDto.getNewPassword());
        return ResponseEntity.status(200).body(BaseResponseBody.of(0, null));
    }

    @Operation(summary = "닉네임 변경", description = "자신의 닉네임을 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "닉네임 변경 성공"),
            @ApiResponse(responseCode = "500", description = "닉네임 변경 실패")
    })
    @PutMapping("/nickname")
    public ResponseEntity<? extends BaseResponseBody> modifyNickname(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Validated @RequestBody NicknameRequestDto nicknameRequestDto
    ){
        userService.modifyNickname(userDetails.getId(), nicknameRequestDto.getNickname());
        return ResponseEntity.status(200).body(BaseResponseBody.of(0, null));
    }


    @Operation(summary = "이메일 변경", description = "자신의 이메일을 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 변경 성공"),
            @ApiResponse(responseCode = "500", description = "이메일 변경 실패")
    })
    @PutMapping("/email")
    public ResponseEntity<? extends BaseResponseBody> modifyEmail(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Validated@RequestBody EmailRequestDto emailRequestDto
    ){
        userService.modifyEmail(userDetails.getId(), emailRequestDto.getEmail());
        return ResponseEntity.status(200).body(BaseResponseBody.of(0, null));
    }
}

