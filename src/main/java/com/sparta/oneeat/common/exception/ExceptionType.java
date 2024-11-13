package com.sparta.oneeat.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

@Getter
public enum ExceptionType {
    // 에러 열거
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S-001", "서버 내부 오류입니다."),

    // Ai
    AI_ACCESS_DENIED(HttpStatus.FORBIDDEN, "A-001", "권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "A-002", "리소스를 찾을 수 없습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "A-003", "요청 형식이 잘못되었습니다."),

    // Menu
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "M-001", "메뉴가 생성되지 않았습니다."),
    MENU_INVALID_REQUEST(HttpStatus.BAD_REQUEST, "M-002", "요청 형식이 잘못되었습니다.");

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
