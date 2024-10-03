package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.dto.JsonResponse;
import com.sbackjung.transferstay.service.LikeAccommodationService;
import com.sbackjung.transferstay.utils.UserIdHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Tag(name = "Assignment Post")
public class LikeAccommodationController {

  private final LikeAccommodationService likeAccommodationService;

  @Operation(summary = "게시물 찜하기", description = "특정 게시물을 찜 처리합니다.")
  @PostMapping("/{postId}/likes")
  public ResponseEntity<JsonResponse> addLike(@PathVariable Long postId) {
    Long userId = UserIdHolder.getUserIdFromToken();
    likeAccommodationService.addLike(userId, postId);
    JsonResponse response = new JsonResponse(HttpStatus.OK.value(), "게시물을 찜하였습니다.", null);

    log.info("[API] addLike");
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "게시물 찜 취소", description = "특정 게시물에 대한 찜을 취소합니다.")
  @DeleteMapping("/{postId}/likes")
  public ResponseEntity<JsonResponse> removeLike(@PathVariable Long postId) {
    Long userId = UserIdHolder.getUserIdFromToken();
    likeAccommodationService.removeLike(userId, postId);
    JsonResponse response = new JsonResponse(HttpStatus.OK.value(), "찜하기가 취소되었습니다.", null);
    log.info("[API] removeLike");
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "찜 여부 조회", description = "특정 게시물에 대해 사용자가 찜했는지 여부를 확인합니다.")
  @GetMapping("/{postId}/likes")
  public ResponseEntity<JsonResponse> isLiked(@PathVariable Long postId) {
    Long userId = UserIdHolder.getUserIdFromToken();
    boolean isLiked = likeAccommodationService.isPostLikedByUser(userId, postId);
    JsonResponse response = new JsonResponse(HttpStatus.OK.value(), "찜 여부 조회 완료.", isLiked);
    log.info("[API] isLiked");
    return ResponseEntity.ok(response);
  }
}
