package com.sparta.oneeat.common.exception;

import com.sparta.oneeat.common.response.BaseResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(CustomException.class)
    public ResponseEntity<? extends BaseResponseBody> exceptions(CustomException e) {

        BaseResponseBody errorResponse = BaseResponseBody.error(e.getExceptionType().getErrorCode(), e.getMessage());
        return ResponseEntity.status(e.getExceptionType().getHttpStatus()).body(errorResponse);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<? extends BaseResponseBody> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        BindingResult bindingResult = e.getBindingResult();
        String errorCode = bindingResult.getFieldErrors().get(0).getCode();
        String errorMessage = bindingResult.getFieldErrors().get(0).getDefaultMessage();

        BaseResponseBody errorResponse = BaseResponseBody.error(errorCode, errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<? extends BaseResponseBody> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {

        String errorCode = "Invalid Data Format";
        String errorMessage = "입력 형식이 유효하지 않습니다. 형식을 다시 확인해주세요.";
        BaseResponseBody errorResponse = BaseResponseBody.error(errorCode, errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}