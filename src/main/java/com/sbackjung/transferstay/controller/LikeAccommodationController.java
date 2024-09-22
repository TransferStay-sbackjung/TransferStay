package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.dto.JsonResponse;
import com.sbackjung.transferstay.service.LikeAccommodationService;
import com.sbackjung.transferstay.utils.UserIdHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
  public ResponseEntity<JsonResponse> addLike(@PathVariable Long postId) {
    Long userId = UserIdHolder.getUserIdFromToken();
    likeAccommodationService.addLike(userId, postId);
    JsonResponse response = new JsonResponse(HttpStatus.OK.value(), "게시물을 찜하였습니다.", null);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{postId}/likes")
  public ResponseEntity<JsonResponse> removeLike(@PathVariable Long postId) {
    Long userId = UserIdHolder.getUserIdFromToken();
    likeAccommodationService.removeLike(userId, postId);
    JsonResponse response = new JsonResponse(HttpStatus.OK.value(), "찜하기가 취소되었습니다.", null);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{postId}/likes")
  public ResponseEntity<JsonResponse> isLiked(@PathVariable Long postId) {
    Long userId = UserIdHolder.getUserIdFromToken();
    boolean isLiked = likeAccommodationService.isPostLikedByUser(userId, postId);
    JsonResponse response = new JsonResponse(HttpStatus.OK.value(), "찜 여부 조회 완료.", isLiked);
    return ResponseEntity.ok(response);
  }
}
