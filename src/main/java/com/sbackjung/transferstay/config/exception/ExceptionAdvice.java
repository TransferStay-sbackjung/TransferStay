package com.sbackjung.transferstay.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> MethodArgumentNotValidException(MethodArgumentNotValidException exception){

        //ErrorCode 에서 미리 Enum 값으로 지정한 코드, 이름 출력
        int code = ErrorCode.BAD_REQUEST.getCode();
        String message = ErrorCode.BAD_REQUEST.getMessage();

        //Exception 을 발생시킨 곳에서 보내는 구체적인 오류 명 (ex. 이메일 정보가 정확하지 않습니다)
        String detail = "잘못된 요청입니다. 인자값을 확인해주세요.";

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(code, message, detail));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> BadRequestException(BadRequestException exception){

        //ErrorCode 에서 미리 Enum 값으로 지정한 코드, 이름 출력
        int code = ErrorCode.BAD_REQUEST.getCode();
        String message = ErrorCode.BAD_REQUEST.getMessage();

        //Exception 을 발생시킨 곳에서 보내는 구체적인 오류 명 (ex. 이메일 정보가 정확하지 않습니다)
        String detail = exception.getMessage();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(code, message, detail));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> CustomException(CustomException exception){

        //ErrorCode 에서 미리 Enum 값으로 지정한 코드, 이름 출력
        int code = exception.getErrorCode().getCode();
        String message = exception.getErrorCode().getMessage();

        //Exception 을 발생시킨 곳에서 보내는 구체적인 오류 명 (ex. 이메일 정보가 정확하지 않습니다)
        String detail = exception.getDetail();

        return ResponseEntity.status(code).body(new ErrorResponse(code, message, detail));
    }
}
