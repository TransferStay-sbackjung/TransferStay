package com.sbackjung.transferstay.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400 Bad Request
    BAD_REQUEST(400, "잘못된 요청 정보입니다."),
    EMAIL_SENDING_ERROR(500,"서버에서 메세지 전송에 실패하였습니다."),
    INVALID_EMAIL_VERIFY(400,"잘못된 인증입니다."),
    EMAIL_EXITS(400,"해당 이메일은 존재합니다."),
    PASSWORD_NOT_MATCH(400,"비밀번호 확인이 일치하지않습니다."),
    INVALID_ENCODING_ERROR(400,"잘못된 인코딩 방식입니다.");
    private final int code;
    private final String message;

}
