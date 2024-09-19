package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.service.PaymentService;
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
@RequestMapping("/payments")
public class PaymentController {
  private final PaymentService paymentService;


  @PostMapping("/{assignmentPostId}")
  public ResponseEntity<String> processPayment(@PathVariable Long assignmentPostId, @RequestParam Long userId) {
    try {
      paymentService.processPayment(assignmentPostId, userId);
      return ResponseEntity.ok("결제 성공");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body("잔액 부족");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 실패");
    }
  }

  @PostMapping("/confirm/{assignmentPostId}")
  public ResponseEntity<String> confirmPayment(@PathVariable Long assignmentPostId, @RequestParam Long userId) {
    try {
      paymentService.completePayment(assignmentPostId, userId);
      return ResponseEntity.ok("결제확정");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body("escrow not found");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제확정 실패");
    }
  }
}
