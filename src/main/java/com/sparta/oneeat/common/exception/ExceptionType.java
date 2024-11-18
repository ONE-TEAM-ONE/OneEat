package com.sparta.oneeat.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionType {
    // 에러 열거
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S-001", "서버 내부 오류입니다."),

    // User
    USER_PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED,"U-001","일치하지 않는 비밀번호 입니다."),
    USER_NOT_EXIST(HttpStatus.NOT_FOUND, "U-002", "회원을 찾을 수 없습니다."),
    USER_NOT_SOFT_DELETE(HttpStatus.BAD_REQUEST, "U-003", "회원이 숨김처리 되어있지 않습니다."),
    USER_EXIST_USERNAME(HttpStatus.BAD_REQUEST, "U-005", "이미 사용하고 있는 유저 네임 입니다."),
    USER_EXIST_NICKNAME(HttpStatus.BAD_REQUEST, "U-004", "이미 사용하고 있는 닉네임 입니다."),
    USER_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "U-005", "이미 사용하고 있는 이메일 입니다."),
    USER_EXIST_ADDRESS(HttpStatus.BAD_REQUEST, "U-006", "이미 등록된 주소 입니다."),
    USER_NOT_EXIST_ADDRESS(HttpStatus.BAD_REQUEST, "U-007", "등록된 주소가 존재하지 않습니다."),
    USER_NOT_SOFT_DELETE_ADDRESS(HttpStatus.BAD_REQUEST, "U-008", "주소가 숨김처리 되어있지 않습니다."),

    // Ai
    AI_ACCESS_DENIED(HttpStatus.FORBIDDEN, "A-001", "권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "A-002", "리소스를 찾을 수 없습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "A-003", "요청 형식이 잘못되었습니다."),

    // Menu
    MENU_INVALID_REQUEST(HttpStatus.BAD_REQUEST, "M-001", "요청 형식이 잘못되었습니다."),
    MENU_ACCESS_DENIED(HttpStatus.FORBIDDEN, "M-002", "권한이 없습니다."),


    // 주문
    ORDER_NOT_EXIST(HttpStatus.NOT_FOUND, "O-001", "해당 주문이 존재하지 않습니다."),
    MENU_PRICE_MISMATCH(HttpStatus.BAD_REQUEST, "O-002", "사용자가 제공한 메뉴가격과 DB에 저장된 메뉴가격이 일치하지 않습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "O-003", "해당 자원에 대한 권한이 없습니다."),
    CANCLE_NOT_ALLOW(HttpStatus.BAD_REQUEST, "O-004", "해당 주문을 취소할 수 없습니다."),
    ALREADY_CANCELLED(HttpStatus.BAD_REQUEST, "O-005", "이미 취소된 주문입니다."),
    MODIFY_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "O-006", "상태 수정이 가능한 주문이 아닙니다."),
    PRICE_MISMATCH(HttpStatus.BAD_REQUEST, "O-007", "메뉴 가격이 일치하지 않습니다."),
    TOTAL_PRICE_MISMATCH(HttpStatus.BAD_REQUEST, "O-008", "총 가격이 일치하지 않습니다."),

    // 리뷰
    REVIEW_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "R-001", "해당 주문의 리뷰가 존재합니다"),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "R-002", "해당 리뷰는 존재하지 않습니다."),
    REVIEW_ACCESS_DENIED(HttpStatus.FORBIDDEN, "R-003", "해당 리뷰에 대한 권한이 없습니다."),
    SOFT_DELETE_ONLY(HttpStatus.BAD_REQUEST, "R-004", "숨김 처리된 리뷰만 삭제할 수 있습니다."),
    ONLY_ADMIN_ACCESS(HttpStatus.FORBIDDEN, "R-005", "관리자만 접근할 수 있습니다."),

    // 결제
    PAYMENT_NOT_EXIST(HttpStatus.NOT_FOUND, "P-001", "해당 결제가 존재하지 않습니다."),
    ORDER_PAYMENT_MISMATCHED(HttpStatus.BAD_REQUEST, "P-002", "해당 주문의 결제가 아닙니다."),
    PAYMENT_ALREADY_PROCESSED(HttpStatus.BAD_REQUEST, "P-003", "이미 처리된 결제입니다."),

    // 카테고리
    CATEGORY_ACCESS_DENIED(HttpStatus.FORBIDDEN, "C-001", "관리자만 접근할 수 있습니다."),
    CATEGORY_DUPLICATED(HttpStatus.BAD_REQUEST, "C-002", "중복된 카테고리명 입니다.");


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
