package com.sbackjung.transferstay.service;

import com.sbackjung.transferstay.Enum.EscrowStatus;
import com.sbackjung.transferstay.Enum.PostStatus;
import com.sbackjung.transferstay.config.exception.CustomException;
import com.sbackjung.transferstay.config.exception.ErrorCode;
import com.sbackjung.transferstay.domain.AssignmentPost;
import com.sbackjung.transferstay.domain.Escrow;
import com.sbackjung.transferstay.domain.Transaction;
import com.sbackjung.transferstay.domain.UserDomain;
import com.sbackjung.transferstay.repository.AssignmentPostRepository;
import com.sbackjung.transferstay.repository.EscrowRepository;
import com.sbackjung.transferstay.repository.TransactionRepository;
import com.sbackjung.transferstay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class PaymentService {

  private final AssignmentPostRepository assignmentPostRepository;
  private final EscrowRepository escrowRepository;
  private final UserRepository userRepository;
  private final TransactionRepository transactionRepository;

  @Transactional
  public void processPayment(Long assignmentId, Long userId) {
    // 1. 게시글 정보 조회
    AssignmentPost assignmentPost = assignmentPostRepository.findById(assignmentId)
        .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST, "해당 게시글을 찾을 수 없습니다."));

    UserDomain user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST, "사용자 정보를 찾을 수 없습니다."));

    long userBalance = user.getAmount();
    // 2. 잔액 확인
    if (userBalance >= assignmentPost.getPrice()) {

      // 에스크로 생성 및 저장
      Escrow escrow = Escrow.builder()
          .assignmentPost(assignmentPost)
          .buyerId(userId)
          .sellerId(assignmentPost.getSellerId())
          .amount(assignmentPost.getPrice())
          .status(EscrowStatus.IN_PROGRESS)
          .build();

      escrowRepository.save(escrow);

      // trnasaction 생성 (결제내역 기록)
      Transaction transaction = Transaction.builder()
          .userId(userId)
          .amount(assignmentPost.getPrice())
          .type(true)
          .description("구매 결제")
          .balance(userBalance - assignmentPost.getPrice()) // 잔액 업데이트
          .build();
      transactionRepository.save(transaction);

      // user 잔액 업데이트
      user.setAmount(userBalance - assignmentPost.getPrice());
      userRepository.save(user);
      // 거래 상태 업데이트
      assignmentPost.setStatus(PostStatus.TRANSACTION_IN_PROGRESS);
    } else {
      // 잔액이 부족한 경우
      throw new CustomException(ErrorCode.BAD_REQUEST, "결제할 금액이 부족합니다.");
    }
  }

  @Transactional
  public void completePayment(Long assignmentId, Long userId) {
    // 게시글 정보 조회
    AssignmentPost assignmentPost = assignmentPostRepository.findById(assignmentId)
        .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST, "해당 게시글을 찾을 수 없습니다."));

    // 에스크로 상태 변경
    Escrow escrow = escrowRepository.findByAssignmentPostId(assignmentId)
        .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST, "해당 에스크로 정보를 찾을 수 없습니다."));

    escrow.setStatus(EscrowStatus.COMPLETED);
    assignmentPost.setStatus(PostStatus.TRANSACTION_COMPLETED);

    // 판매자의 잔액 업데이트 (판매자에게 금액 송금)
    UserDomain seller = userRepository.findById(assignmentPost.getSellerId())
            .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST, " 판매자 정보를 찾을 수 없습니다."));

    long sellerBalance = seller.getAmount();
    seller.setAmount(sellerBalance - escrow.getAmount());
    userRepository.save(seller);

    // 트랜잭션 기록 (판매자에게 입금내역 기록)
    Transaction transaction = Transaction.builder()
        .userId(seller.getUserId())
        .amount(escrow.getAmount())
        .type(true)
        .description("양도 게시글 판매 대금")
        .balance(seller.getAmount()) // 거래 이후 잔액
        .build();
    transactionRepository.save(transaction);

    // 게시글 상태 변경 (거래완료)
    assignmentPost.setStatus(PostStatus.TRANSACTION_COMPLETED);
    assignmentPostRepository.save(assignmentPost);

  }
}
