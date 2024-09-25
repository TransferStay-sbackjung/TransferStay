package com.sbackjung.transferstay.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400 Bad Request
    BAD_REQUEST(400, "잘못된 요청 정보입니다."),
    INVALID_AMOUNT(400, "유효하지 않은 금액입니다."),
    INSUFFICIENT_FUNDS(400,  "잔액이 부족합니다."),
    EXCEEDS_MAX_AMOUNT(400, "충전 금액이 최대 한도를 초과할 수 없습니다."),
    // 401 UnAuthorize
    UN_AUTHORIZE(401, "권한이 없는 사용자입니다."),
    // 404 Not Found
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    POST_NOT_FOUND(404, "게시글을 찾울 수 없습니다."),
    // 500 Server Error
    INTER_SERVER_ERROR(500,"서버 내부의 에러입니다."),
    DEPOSIT_RECHARGE_ERROR(500, "충전 처리 중 오류가 발생했습니다."),
    DEPOSIT_BALANCE_ERROR(500, "잔액 조회 중 오류가 발생했습니다."),
    DEPOSIT_REFUND_ERROR(500,  "환불 처리 중 오류가 발생했습니다.");
    private final int code;
    private final String message;

}
