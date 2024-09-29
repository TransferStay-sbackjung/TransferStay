package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.config.exception.CustomException;
import com.sbackjung.transferstay.config.exception.ErrorCode;
import com.sbackjung.transferstay.dto.CustomOAuth2User;
import com.sbackjung.transferstay.dto.JsonResponse;
import com.sbackjung.transferstay.dto.UserDetailsDto;
import com.sbackjung.transferstay.service.DepositRechargeService;
import com.sbackjung.transferstay.utils.UserIdHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Payment", description = "결제 처리 API")
public class DepositRechargeController {

  private final DepositRechargeService depositRechargeService;

  // 충전
  @Operation(summary = "예치금 충전", description = "사용자의 예치금을 충전합니다.")
  @PostMapping("/recharge")
  public ResponseEntity<JsonResponse> rechargeDeposit(
      @Parameter(description = "충전할 금액", required = true, schema = @Schema(type = "long", example = "10000"))
      @RequestBody Long amount) {
    Long userId = UserIdHolder.getUserIdFromToken();
    Long balance = depositRechargeService.rechargeDeposit(userId, amount);
    JsonResponse response = new JsonResponse(HttpStatus.OK.value(), "충전이 완료되었습니다.", balance);
    return ResponseEntity.ok(response);
  }

  // 환불
  @Operation(summary = "예치금 환불", description = "사용자의 예치금을 환불합니다.")
  @PostMapping("/refund")
  public ResponseEntity<JsonResponse> refundDeposit(
      @Parameter(description = "환불할 금액", required = true, schema = @Schema(type = "long", example = "5000"))
      @RequestBody Long amount) {
    Long userId = UserIdHolder.getUserIdFromToken();
    Long balance = depositRechargeService.refundDeposit(userId, amount);
    JsonResponse response = new JsonResponse(HttpStatus.OK.value(), "환불이 완료되었습니다.", balance);
    return ResponseEntity.ok(response);
  }

  // 조회
  @Operation(summary = "예치금 잔액 조회", description = "사용자의 예치금 잔액을 조회합니다.")
  @GetMapping
  public ResponseEntity<JsonResponse> getDepositBalance() {
    Long userId = UserIdHolder.getUserIdFromToken();
    Long balance = depositRechargeService.getDepositBalance(userId);
    JsonResponse response = new JsonResponse(HttpStatus.OK.value(), "현재 잔액은 " + balance + "원입니다.", balance);
    return ResponseEntity.ok(response);
  }
}