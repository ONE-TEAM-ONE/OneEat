package com.sparta.oneeat.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@AllArgsConstructor
public class SignupRequestDto {
    @NotBlank
    @Pattern(regexp = "^[a-z0-9]{4,10}$",
            message = "(4~10자) 소문자, 숫자 사용 가능")
    private String username;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{8,15}$",
            message = "(8~15자) 대소문자, 숫자, 특수문자 사용 가능")
    private String password;

    @NotBlank
    private String nickname;

    @Email
    private String email;

    private String address;
}