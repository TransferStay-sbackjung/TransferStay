package com.sbackjung.transferstay.service;

import com.sbackjung.transferstay.config.exception.CustomException;
import com.sbackjung.transferstay.config.exception.ErrorCode;
import com.sbackjung.transferstay.domain.AssignmentPost;
import com.sbackjung.transferstay.domain.LikeAccommodation;
import com.sbackjung.transferstay.repository.AssignmentPostRepository;
import com.sbackjung.transferstay.repository.LikeAccommodationRepository;
import jakarta.transaction.Transactional;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeAccommodationService {

  private final LikeAccommodationRepository likeAccommodationRepository;
  private final AssignmentPostRepository assignmentPostRepository;

  public void addLike(Long userId, Long postId) {
    // 게시글 정보 조회
    AssignmentPost post = assignmentPostRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST, "해당 게시글을 찾을 수 없습니다."));

    // 해당 게시글에 대한 사용자의 좋아요 여부 확인
    Optional<LikeAccommodation> existingLike = likeAccommodationRepository.findByUserIdAndAssignmentPostId(userId, postId);
    if (existingLike.isEmpty()) {
      // 좋아요가 존재하지 않으면 새로운 좋아요를 추가
      LikeAccommodation newLike = LikeAccommodation.builder()
          .userId(userId)
          .assignmentPost(post)
          .build();
      likeAccommodationRepository.save(newLike);
    } else {
      // 이미 좋아요가 존재하는 경우 예외 처리
      throw new CustomException(ErrorCode.BAD_REQUEST, "이미 이 게시물을 찜했습니다.");
    }
  }

  @Transactional
  public void removeLike(Long userId, Long postId) {

    if (!assignmentPostRepository.existsById(postId)) {
      throw new CustomException(ErrorCode.POST_NOT_FOUND, "게시글을 찾을 수 없습니다.");
    }

    // 해당 게시글에 대한 사용자의 좋아요 여부 확인
    Optional<LikeAccommodation> existingLike = likeAccommodationRepository.findByUserIdAndAssignmentPostId(userId, postId);
    if (existingLike.isPresent()) {
      // 좋아요가 존재하면 삭제
      likeAccommodationRepository.deleteByUserIdAndAssignmentPostId(userId, postId);
    } else {
      // 좋아요가 존재하지 않으면 예외 처리
      throw new CustomException(ErrorCode.BAD_REQUEST, "찜한 게시물이 아닙니다.");
    }
  }

  public boolean isPostLikedByUser(Long userId, Long postId) {

    if (!assignmentPostRepository.existsById(postId)) {
      throw new CustomException(ErrorCode.POST_NOT_FOUND, "게시글을 찾을 수 없습니다.");
    }

    // 게시글이 사용자의 좋아요 목록에 있는지 확인
    return likeAccommodationRepository.findByUserIdAndAssignmentPostId(userId, postId).isPresent();
  }
}
