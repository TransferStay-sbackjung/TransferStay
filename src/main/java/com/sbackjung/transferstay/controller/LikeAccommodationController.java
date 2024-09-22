package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.service.LikeAccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/assignment-posts")
@RequiredArgsConstructor
public class LikeAccommodationController {

  private final LikeAccommodationService likeAccommodationService;

  @PostMapping("/{postId}/likes")
  public ResponseEntity<String> addLike(@PathVariable Long postId, @RequestParam Long userId) {
    likeAccommodationService.addLike(userId, postId);
    return ResponseEntity.ok("게시물을 찜하였습니다.");
  }

  @DeleteMapping("/{postId}/likes")
  public ResponseEntity<String> removeLike(@PathVariable Long postId, @RequestParam Long userId) {
    likeAccommodationService.removeLike(userId, postId);
    return ResponseEntity.ok("찜하기가 취소되었습니다.");
  }

  @GetMapping("/{postId}/likes")
  public ResponseEntity<Boolean> isLiked(@PathVariable Long postId, @RequestParam Long userId) {
    boolean isLiked = likeAccommodationService.isPostLikedByUser(userId, postId);
    return ResponseEntity.ok(isLiked);
  }
}
