package com.sbackjung.transferstay.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // 400 Bad Request
    BAD_REQUEST(400, "잘못된 요청 정보입니다.");


    private final int code;
    private final String message;

}
