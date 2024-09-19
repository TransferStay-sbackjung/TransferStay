package com.sbackjung.transferstay.service;

import com.sbackjung.transferstay.Enum.EscrowStatus;
import com.sbackjung.transferstay.Enum.PostStatus;
import com.sbackjung.transferstay.domain.AssignmentPost;
import com.sbackjung.transferstay.domain.Escrow;
import com.sbackjung.transferstay.repository.AssignmentPostRepository;
import com.sbackjung.transferstay.repository.EscrowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class PaymentService {

  private final AssignmentPostRepository assignmentPostRepository;
  private final EscrowRepository escrowRepository;

  @Transactional
  public void processPayment(Long assignmentId, Long userId) {
    // 1. 게시글 정보 조회
    AssignmentPost assignmentPost = assignmentPostRepository.findById(assignmentId)
        .orElseThrow(() -> new IllegalArgumentException("Assignment not found"));

    // 임시 유저 잔액 설정 (실제 구현 시 UserRepository를 사용하여 유저 정보를 조회)
    long userBalance = 1000000L;

    // 2. 잔액 확인
    if (userBalance >= assignmentPost.getPrice()) {

      // todo : 유저 transaction 연동 및 차액 관리

      // 에스크로 생성 및 저장
      Escrow escrow = Escrow.builder()
          .assignmentPost(assignmentPost)
          .buyerId(userId)
          .sellerId(assignmentPost.getSellerId())
          .amount(assignmentPost.getPrice())
          .status(EscrowStatus.IN_PROGRESS)
          .build();

      escrowRepository.save(escrow);

      // 거래 상태 업데이트
      assignmentPost.setStatus(PostStatus.TRANSACTION_IN_PROGRESS);
    } else {
      // 잔액이 부족한 경우
      throw new IllegalArgumentException("잔액 부족");
    }
  }

  @Transactional
  public void completePayment(Long assignmentId, Long userId) {
    // 게시글 정보 조회
    AssignmentPost assignmentPost = assignmentPostRepository.findById(assignmentId)
        .orElseThrow(() -> new IllegalArgumentException("Assignment not found"));

    // 에스크로 상태 변경
    Escrow escrow = escrowRepository.findByAssignmentPostId(assignmentId)
        .orElseThrow(() -> new IllegalArgumentException("Assignment not found"));

    // todo : transacton 로직 추가 (판매자에게 돈 입금, 구매자에게 돈 출금)

    escrow.setStatus(EscrowStatus.COMPLETED);

    assignmentPost.setStatus(PostStatus.TRANSACTION_COMPLETED);

  }
}
