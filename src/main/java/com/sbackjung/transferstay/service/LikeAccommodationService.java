package com.sbackjung.transferstay.service;

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
    AssignmentPost post = assignmentPostRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));

    Optional<LikeAccommodation> existingLike = likeAccommodationRepository.findByUserIdAndAssignmentPostId(
        userId, postId);
    if (existingLike.isEmpty()) {

      LikeAccommodation newLike = LikeAccommodation.builder()
          .userId(userId)
          .assignmentPost(post)
          .build();
      likeAccommodationRepository.save(newLike);
    } else {
      throw new IllegalStateException("이미 이 게시물을 찜했습니다.");
    }
  }


  @Transactional
  public void removeLike(Long userId, Long postId) {
    Optional<LikeAccommodation> existingLike = likeAccommodationRepository.findByUserIdAndAssignmentPostId(
        userId, postId);
    if (existingLike.isPresent()) {
      likeAccommodationRepository.deleteByUserIdAndAssignmentPostId(userId, postId);
    } else {
      throw new IllegalStateException("찜한 게시물이 아닙니다.");
    }
  }

  public boolean isPostLikedByUser(Long userId, Long postId) {
    return likeAccommodationRepository.findByUserIdAndAssignmentPostId(userId, postId).isPresent();
  }
}