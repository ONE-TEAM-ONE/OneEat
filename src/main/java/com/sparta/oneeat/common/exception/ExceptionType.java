package com.sparta.oneeat.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionType {
    // 에러 열거
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S-001", "서버 내부 오류입니다."),

    // 주문
    ORDER_NOT_EXIST(HttpStatus.NOT_FOUND, "O-001", "해당 주문이 존재하지 않습니다."),
    MENU_PRICE_MISMATCH(HttpStatus.BAD_REQUEST, "O-002", "사용자가 제공한 메뉴가격과 DB에 저장된 메뉴가격이 일치하지 않습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "O-003", "해당 자원에 대한 권한이 없습니다."),
    CANCLE_NOT_ALLOW(HttpStatus.BAD_REQUEST, "O-004", "해당 주문을 취소할 수 없습니다."),
    ALREADY_CANCELLED(HttpStatus.BAD_REQUEST, "O-005", "이미 취소된 주문입니다."),
    MODIFY_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "O-006", "상태 수정이 가능한 주문이 아닙니다."),
  
    // 리뷰
    REVIEW_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "R-001", "해당 주문의 리뷰가 존재합니다");


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
