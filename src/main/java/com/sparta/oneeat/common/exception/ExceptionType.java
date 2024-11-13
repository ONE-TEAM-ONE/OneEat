package com.sparta.oneeat.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionType {
    // 에러 열거
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S-001", "서버 내부 오류입니다."),

    // Auth
    AUTH_DUPLICATE_USERNAME(HttpStatus.CONFLICT,"A-001","중복된 아이디를 가진 사용자가 존재합니다."),
    AUTH_DUPLICATE_NICKNAME(HttpStatus.CONFLICT,"A-002","중복된 닉네임을 가진 사용자가 존재합니다.");

    // 상태, 에러 코드, 메시지
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    // 생성자
    ExceptionType(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }
}
