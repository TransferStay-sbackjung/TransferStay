package com.sbackjung.transferstay.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> BadRequestException(BadRequestException exception){
        int code = ErrorCode.BAD_REQUEST.getCode();
        String message = ErrorCode.BAD_REQUEST.getMessage();
        String detail = exception.getMessage();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(code, message, detail));
    }
}
