package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.dto.JsonResponse;
import com.sbackjung.transferstay.service.PaymentService;
import com.sbackjung.transferstay.utils.UserIdHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/assignment-posts")
@Tag(name = "Payment", description = "결제 처리 API")
public class PaymentController {
  private final PaymentService paymentService;

  @Operation(summary = "일반 결제 처리", description = "일반 결제를 처리합니다.")
  @PostMapping("/{assignmentPostId}/payments")
  public ResponseEntity<JsonResponse> processPayment(@PathVariable Long assignmentPostId) {
    Long userId = UserIdHolder.getUserIdFromToken();
    try {
      paymentService.processPayment(assignmentPostId, userId);
      JsonResponse response = new JsonResponse(HttpStatus.OK.value(), "결제 성공", null);
      return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
      JsonResponse response = new JsonResponse(HttpStatus.BAD_REQUEST.value(), "잔액 부족", null);
      return ResponseEntity.badRequest().body(response);
    } catch (Exception e) {
      JsonResponse response = new JsonResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "결제 실패", null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  @PostMapping("/{assignmentPostId}/payments/confirm")
  public ResponseEntity<JsonResponse> confirmPayment(@PathVariable Long assignmentPostId) {
    Long userId = UserIdHolder.getUserIdFromToken(); // UserIdHolder에서 userId 가져오기
    try {
      paymentService.completePayment(assignmentPostId, userId);
      JsonResponse response = new JsonResponse(HttpStatus.OK.value(), "결제확정", null);
      return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
      JsonResponse response = new JsonResponse(HttpStatus.BAD_REQUEST.value(), "escrow not found", null);
      return ResponseEntity.badRequest().body(response);
    } catch (Exception e) {
      JsonResponse response = new JsonResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "결제확정 실패", null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }
}
