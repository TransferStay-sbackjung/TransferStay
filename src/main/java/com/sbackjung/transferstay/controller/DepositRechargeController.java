package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.config.exception.CustomException;
import com.sbackjung.transferstay.config.exception.ErrorCode;
import com.sbackjung.transferstay.dto.CustomOAuth2User;
import com.sbackjung.transferstay.dto.JsonResponse;
import com.sbackjung.transferstay.dto.UserDetailsDto;
import com.sbackjung.transferstay.service.DepositRechargeService;
import com.sbackjung.transferstay.utils.UserIdHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deposit")
@Slf4j
public class DepositRechargeController {

  private final DepositRechargeService depositRechargeService;

  // 충전
  @PostMapping("/recharge")
  public ResponseEntity<JsonResponse> rechargeDeposit(@RequestBody Long amount) {
    Long userId = UserIdHolder.getUserIdFromToken();
    depositRechargeService.rechargeDeposit(userId, amount);
    JsonResponse response = new JsonResponse(HttpStatus.OK.value(), "충전이 완료되었습니다.", null);
    return ResponseEntity.ok(response);
  }

  // 환불
  @PostMapping("/refund")
  public ResponseEntity<JsonResponse> refundDeposit(@RequestBody Long amount) {
    Long userId = UserIdHolder.getUserIdFromToken();
    depositRechargeService.refundDeposit(userId, amount);
    JsonResponse response = new JsonResponse(HttpStatus.OK.value(), "환불이 완료되었습니다.", null);
    return ResponseEntity.ok(response);
  }

  // 조회
  @GetMapping
  public ResponseEntity<JsonResponse> getDepositBalance() {
    Long userId = UserIdHolder.getUserIdFromToken();
    Long balance = depositRechargeService.getDepositBalance(userId);
    JsonResponse response = new JsonResponse(HttpStatus.OK.value(), "현재 잔액은 " + balance + "원입니다.", balance);
    return ResponseEntity.ok(response);
  }
}