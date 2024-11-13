package com.sparta.oneeat.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionType {
    // 에러 열거
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S-001", "서버 내부 오류입니다."),

    // Auth
    AUTH_INVALID_SIGNATURE_TOKEN(HttpStatus.UNAUTHORIZED, "A-001", "유효하지 않은 JWT 서명입니다."),
    AUTH_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "A-002", "만료된 JWT 토큰입니다."),
    AUTH_UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "A-003", "지원되지 않는 JWT 토큰입니다."),
    AUTH_EMPTY_CLAIMS_TOKEN(HttpStatus.BAD_REQUEST, "A-004", "잘못된 JWT 토큰입니다."),
    AUTH_NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND, "A-005", "토큰을 확인할 수 없습니다."),
    AUTH_UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED, "A-006", "유효하지 않은 토큰입니다."),
    AUTH_UNAUTHORIZED_ERROR(HttpStatus.UNAUTHORIZED, "A-007", "토큰 인증 과정에서 오류가 발생했습니다."),
    AUTH_INVALID_PASSWORD(HttpStatus.UNPROCESSABLE_ENTITY, "A-008", "비밀번호가 유효하지 않습니다. 비밀번호를 입력해 주세요."),
    AUTH_DUPLICATE_USERNAME(HttpStatus.CONFLICT,"A-009","중복된 아이디를 가진 사용자가 존재합니다."),
    AUTH_DUPLICATE_NICKNAME(HttpStatus.CONFLICT,"A-010","중복된 닉네임을 가진 사용자가 존재합니다.");

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
