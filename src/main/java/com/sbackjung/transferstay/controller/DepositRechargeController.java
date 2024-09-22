package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.config.exception.CustomException;
import com.sbackjung.transferstay.config.exception.ErrorCode;
import com.sbackjung.transferstay.dto.CustomOAuth2User;
import com.sbackjung.transferstay.dto.UserDetailsDto;
import com.sbackjung.transferstay.service.DepositRechargeService;
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
@RequestMapping("/api/deposit")
@Slf4j
public class DepositRechargeController {

  private final DepositRechargeService depositRechargeService;

  @PostMapping("/recharge")
  public ResponseEntity<String> rechargeDeposit(@RequestBody Long amount) {
    try {
      // 현재 인증된 사용자 정보 가져오기
      Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

      log.info("Principal class: {}", principal.getClass().getName());

      // 소셜 로그인 사용자인지 자체 회원가입 사용자인지 체크
      // 소셜 로그인 일때
      if (principal instanceof CustomOAuth2User oAuth2User) {
        log.info(oAuth2User.getName());
        String oauthId = oAuth2User.getName();  // 소셜 로그인 사용자 ID 가져오기
        depositRechargeService.rechargeDepositByOAuthId(oauthId, amount);
      }
      // 자체 로그인 일때
      else if (principal instanceof UserDetailsDto userDetails) {
        String email = userDetails.getUsername();  // 자체 회원가입 사용자의 이메일 가져오기
        depositRechargeService.rechargeDepositByEmail(email, amount);
      } else {
        throw new CustomException(ErrorCode.UN_AUTHORIZE);
      }

      return ResponseEntity.status(HttpStatus.OK).body("충전이 완료되었습니다.");

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (CustomException e) {
      return ResponseEntity.status(e.getErrorCode().getCode()).body(e.getErrorCode().getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorCode.DEPOSIT_RECHARGE_ERROR.getMessage());
    }
  }

  @PostMapping("/refund")
  public ResponseEntity<String> refundDeposit(@RequestBody Long amount) {
    try {
      // 현재 인증된 사용자 정보 가져오기
      Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

      log.info("Principal class: {}", principal.getClass().getName());

      // 소셜 로그인 사용자일 때
      if (principal instanceof CustomOAuth2User oAuth2User) {
        log.info(oAuth2User.getName());
        String oauthId = oAuth2User.getName();  // 소셜 로그인 사용자 ID 가져오기
        depositRechargeService.refundDepositByOAuthId(oauthId, amount);
      }
      // 자체 회원가입 사용자일 때
      else if (principal instanceof UserDetailsDto userDetails) {
        String email = userDetails.getUsername();  // 자체 회원가입 사용자의 이메일 가져오기
        depositRechargeService.refundDepositByEmail(email, amount);
      } else {
        throw new CustomException(ErrorCode.UN_AUTHORIZE);
      }

      return ResponseEntity.status(HttpStatus.OK).body("환불이 완료되었습니다.");

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (CustomException e) {
      return ResponseEntity.status(e.getErrorCode().getCode()).body(e.getErrorCode().getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorCode.DEPOSIT_REFUND_ERROR.getMessage());
    }
  }
}